package happy.mjstudio.playablerecyclerview.view

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.playablerecyclerview.R
import happy.mjstudio.playablerecyclerview.common.VisibleSize
import happy.mjstudio.playablerecyclerview.enum.LoopType
import happy.mjstudio.playablerecyclerview.enum.PlayableType
import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.enum.TargetState
import happy.mjstudio.playablerecyclerview.manager.PlayableManager
import happy.mjstudio.playablerecyclerview.model.Playable
import happy.mjstudio.playablerecyclerview.player.PlayablePlayer
import happy.mjstudio.playablerecyclerview.target.PlayableTarget
import happy.mjstudio.playablerecyclerview.util.attachSnapHelper
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.math.abs
import kotlin.math.max


/**
 * Created by mj on 20, January, 2020
 */
@SuppressLint("Recycle")
class PlayableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    //region Manager

    val manager: PlayableManager = object : PlayableManager {
        override fun pauseAllPlayables() {
            playerPool.forEach { player ->
                if (player.state == PlayerState.PLAYING) {
                    player.pause()
                }
            }
        }

        override fun resumeCurrentPlayable() {
            playPlayable()
        }

        override fun updatePlayables() {
            pauseAllPlayables()
            resumeCurrentPlayable()
        }

        override fun playPlayable(position: Int) = this@PlayableRecyclerView.playPlayable(position)


        override fun pausePlayable(position: Int) = this@PlayableRecyclerView.pausePlayable(position)


        override fun getPlaygingState(position: Int) = this@PlayableRecyclerView.getPlaygingState(position)

        override fun seekToPlayablePlayback(position: Int, ms: Long) = this@PlayableRecyclerView.seekToPlayablePlayback(position, ms)
    }

    //endregion


    //region Variable

    /**
     * Engine Type of [PlayableRecyclerView]
     *
     * default implementation with ExoPlayer
     */
    private var playableType: PlayableType = DEFAULT_PLAYABLE_TYPE

    /**
     * max count of players can play it's content concurrently
     *
     * default = 1
     */
    private var videoPlayingConcurrentMax = DEFAULT_VIDEO_PLAYING_CONCURRENT_MAX

    /**
     * Used [PlayablePlayer] count in [playerPool]
     *
     * default = 2
     */
    private var playerPoolCount = DEFAULT_PLAYER_POOL_COUNT

    private var isAutoPlay = DEFAULT_AUTOPLAY

    private var loopType = DEFAULT_LOOP_TYPE

    private var isPauseDuringInvisible = DEFAULT_PAUSE_DURING_INVISIBLE

    /**
     * List of [PlayablePlayer] used for playback in List
     */
    private var playerPool = listOf<PlayablePlayer>()
        set(value) {
            updatePlayerPriority()
            field = value
        }

    private val playerQueue =
        PriorityQueue<PlayablePlayer>(playerPoolCount) { lhs, rhs ->
            (lhs.latestUsedTimeMs - rhs.latestUsedTimeMs).toInt()
        }

    private val playerQueueLock = Semaphore(1)

    private var firstCandidatePosition = -1

    /** Device Screen Size */
    private val screenWidth: Int = resources.displayMetrics.widthPixels
    private val screenHeight: Int = resources.displayMetrics.heightPixels

    private var isPageSnapping = false
    //endregion

    init {

        context.obtainStyledAttributes(attrs, R.styleable.PlayableRecyclerView, 0, 0).use {
            playableType = PlayableType.parse(it.getInteger(R.styleable.PlayableRecyclerView_playable_type, DEFAULT_PLAYABLE_TYPE.rawValue))

            playerPoolCount = it.getInteger(R.styleable.PlayableRecyclerView_playable_player_pool_count, DEFAULT_PLAYER_POOL_COUNT)

            videoPlayingConcurrentMax =
                it.getInteger(R.styleable.PlayableRecyclerView_playable_player_concurrent_max, DEFAULT_VIDEO_PLAYING_CONCURRENT_MAX)

            isAutoPlay = it.getBoolean(R.styleable.PlayableRecyclerView_playable_autoplay, DEFAULT_AUTOPLAY)

            loopType = LoopType.parse(it.getInteger(R.styleable.PlayableRecyclerView_playable_loop_type, DEFAULT_LOOP_TYPE.rawValue))

            isPauseDuringInvisible = it.getBoolean(R.styleable.PlayableRecyclerView_playable_pause_during_invisible, DEFAULT_PAUSE_DURING_INVISIBLE)
        }

        if (isPageSnapping)
            attachSnapHelper()

        observeScrollEvent()
    }

    private fun generatePlayer(): PlayablePlayer = playableType.generatePlayer(context.applicationContext as Application, loopType)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isInEditMode)
            playerPool = (0 until playerPoolCount).map { generatePlayer() }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playerPool.forEach {
            it.release()
        }
        playerPool = listOf()
    }

    //region Decide First Candidate

    private fun observeScrollEvent() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (isPauseDuringInvisible) {
                    pauseInvisiblePlayers()
                }

                val newCandidatePosition = computeFirstCandidatePosition()
                if (firstCandidatePosition == newCandidatePosition) return
                firstCandidatePosition = newCandidatePosition

                if (isAutoPlay) {
                    playPlayable()
                }
            }
        })
    }

    /**
     * Returns the visible size of the [Playable] surface on the screen.
     * @param childPosition position for item in LayoutManager
     */
    private fun computePlayerVisibleSize(childPosition: Int) =
        computePlayerVisibleSize((findViewHolderForLayoutPosition(childPosition) as? PlayableTarget)?.getPlayableView())

    @Suppress("NAME_SHADOWING")
    private fun computePlayerVisibleSize(playableView: PlayableView?): VisibleSize? {
        val playableView = playableView as? View ?: return null

        val playableWidth = playableView.width
        val playableHeight = playableView.height

        val location = IntArray(2)
        playableView.getLocationInWindow(location)

        val startX = location[0]
        val startY = location[1]

        val endX = startX + playableWidth
        val endY = startY + playableHeight

        val clippedWidth = (if (startX < 0) abs(startX) else 0) + (if (endX > screenWidth) endX - screenWidth else 0)
        val clippedHeight = (if (startY < 0) abs(startY) else 0) + (if (endY > screenHeight) endY - screenHeight else 0)

        val visibleWidth = max(0, endX - startX - clippedWidth)
        val visibleHeight = max(0, endY - startY - clippedHeight)

        return visibleWidth * visibleHeight
    }

    private fun computeFirstCandidatePosition(): Int {
        val layoutManager = layoutManager as? LinearLayoutManager ?: throw RuntimeException("LayoutManager is not LinearLayoutManager")

        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        return (firstVisiblePosition..lastVisiblePosition).maxBy {
            computePlayerVisibleSize(it) ?: 0
        } ?: firstVisiblePosition
    }

    //endregion

    //region Manager Playable State

    private fun getCurrentPlayingVideoCount() = playerPool.count { it.state == PlayerState.PLAYING }

    private fun pauseOldPlayers() {
        if (getCurrentPlayingVideoCount() > videoPlayingConcurrentMax) {
            var pauseCountLatch = getCurrentPlayingVideoCount() - videoPlayingConcurrentMax

            val playersSortedOldest = playerPool.sortedBy { it.latestUsedTimeMs }

            playersSortedOldest.forEach { player ->
                if (pauseCountLatch == 0) return@forEach

                if (player.state == PlayerState.PLAYING) {
                    player.pause()
                    pauseCountLatch -= 1
                }
            }
        }
    }

    private fun playPlayable(position: Int? = null): Boolean {

        val candidatePosition = position ?: firstCandidatePosition

        val adapter = (adapter as? PlayableAdapter<*>) ?: return false
        val playable = adapter.currentList.getOrNull(candidatePosition) ?: return false

        val target: PlayableTarget = getPlayableTargetWithPosition(candidatePosition) ?: return false

        when (target.state) {
            TargetState.ATTACHED -> {
                target.player?.play(playable) ?: return false
            }
            TargetState.DETACHED -> {
                val newPlayer = dequeueOldestPlayer()
                val oldTarget = newPlayer.target
                newPlayer.detach()

                newPlayer.attach(oldTarget, target)
                newPlayer.play(playable)
            }
        }

        pauseOldPlayers()
        return true
    }

    private fun pausePlayable(position: Int): Boolean {
        val target: PlayableTarget = getPlayableTargetWithPosition(position) ?: return false
        target.player?.pause() ?: return false

        return true
    }

    private fun getPlaygingState(position: Int): PlayerState? {
        val target: PlayableTarget = getPlayableTargetWithPosition(position) ?: return null
        return target.player?.state
    }

    private fun seekToPlayablePlayback(position: Int, ms: Long): Boolean {
        val target: PlayableTarget = getPlayableTargetWithPosition(position) ?: return false
        target.player?.seekTo(ms) ?: return false

        return true
    }

    private fun pauseInvisiblePlayers() {
        playerPool.forEach { player ->
            if ((computePlayerVisibleSize(player.target?.getPlayableView()) ?: 0) <= 0)
                player.pause()
        }
    }

    //endregion

    //region Utils

    private fun getPlayableTargetWithView(view: View): PlayableTarget? {
        return getPlayableTargetWithPosition(getChildLayoutPosition(view))
    }

    private fun getPlayableTargetWithPosition(position: Int): PlayableTarget? {
        return findViewHolderForLayoutPosition(position) as? PlayableTarget
    }

    private fun updatePlayerPriority() {
        playerQueueLock.acquire()
        playerQueue.clear()
        playerPool.forEach { playerQueue.offer(it) }
        playerQueueLock.release()
    }

    private fun dequeueOldestPlayer(): PlayablePlayer {
        updatePlayerPriority()
        return playerQueue.peek()?.also { it.updateUsedTime() } ?: throw RuntimeException("WTF")
    }

    //endregion

    companion object {
        private val DEFAULT_PLAYABLE_TYPE = PlayableType.EXOPLAYER
        private const val DEFAULT_PLAYER_POOL_COUNT = 0x02
        private const val DEFAULT_VIDEO_PLAYING_CONCURRENT_MAX = 0x01
        private const val DEFAULT_AUTOPLAY = false
        private val DEFAULT_LOOP_TYPE = LoopType.NONE
        private const val DEFAULT_PAUSE_DURING_INVISIBLE = true
    }

}