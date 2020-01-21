package happy.mjstudio.playablerecyclerview.view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.enum.TargetState
import happy.mjstudio.playablerecyclerview.player.ExoPlayerPlayablePlayer
import happy.mjstudio.playablerecyclerview.player.PlayablePlayer
import happy.mjstudio.playablerecyclerview.target.PlayableTarget
import happy.mjstudio.playablerecyclerview.util.attachSnapHelper
import happy.mjstudio.playablerecyclerview.util.debugE
import kotlinx.android.parcel.Parcelize
import java.util.*
import java.util.concurrent.Semaphore


/**
 * Created by mj on 20, January, 2020
 */
class PlayableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    companion object {
        private val TAG = PlayableRecyclerView::class.java.simpleName

        private const val DEFAULT_PLAYER_COUNT = 0x02
        private const val DEFAULT_VIDEO_PLAYING_CONCURRENT_MAX = 0x01
    }

    //region Variable

    private var videoPlayingConcurrentMax = DEFAULT_VIDEO_PLAYING_CONCURRENT_MAX

    /**
     * Used [PlayablePlayer] count in [playerPool]
     *
     * default = 1
     */
    private var playerCount = DEFAULT_PLAYER_COUNT

    /**
     * List of [PlayablePlayer] used for playback in List
     */
    private var playerPool = listOf<PlayablePlayer<out PlayableTarget>>()
        set(value) {
            updatePlayerPriority()
            field = value
        }

    private val playerQueue =
        PriorityQueue<PlayablePlayer<PlayableTarget>>(playerCount) { lhs, rhs ->
            (lhs.latestUsedTimeMs - rhs.latestUsedTimeMs).toInt()
        }

    private val playerQueueLock = Semaphore(1)

    private var firstCandidatePosition = -1

    /**
     * Device Screen Height
     */
    private val screenHeight: Int = resources.displayMetrics.heightPixels

    private val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    private var isPageSnapping = true
    //endregion

    //region Save/Restore State

    @Parcelize
    private data class SavedState(
        val playerCount: Int
    ) : Parcelable

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState()
        return SavedState(this.playerCount)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? SavedState)?.let {
            this.playerCount = it.playerCount
        }
        super.onRestoreInstanceState(state)
    }

    //endregion

    init {
        // Setting for saving state of view properties
        isSaveEnabled = true

        setLayoutManager(layoutManager)

        if (isPageSnapping)
            attachSnapHelper()

        observeScrollEvent()
    }

    private fun generatePlayer(): PlayablePlayer<out PlayableTarget> =
        ExoPlayerPlayablePlayer(context)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        playerPool = (0 until playerCount).map { generatePlayer() }
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

                val newCandidatePosition = computeFirstCandidatePosition()

                if (firstCandidatePosition == newCandidatePosition) return

                firstCandidatePosition = newCandidatePosition

                debugE(TAG, "new candidate : $firstCandidatePosition")

                playNewCandidate()
            }
        })
    }

    /**
     * Returns the visible region of the video surface on the screen.
     * @param position position for item
     * @return
     */
    private fun computeVisibleItemHeight(position: Int): Int? {
        val child = findViewHolderForLayoutPosition(position)?.itemView ?: return null
        val location = IntArray(2)
        child.getLocationInWindow(location)

        val y = location[1]

        return when {
            y < 0 -> {
                child.height + y
            }
            y + child.height < screenHeight -> {
                child.height
            }
            else -> {
                screenHeight - y
            }
        }
    }

    private fun computeFirstCandidatePosition(): Int {
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        return (firstVisiblePosition..lastVisiblePosition).maxBy {
            computeVisibleItemHeight(it) ?: 0
        } ?: firstVisiblePosition
    }

    //endregion

    //region Play/Pause Video

    private fun getCurrentPlayingVideoCount() = playerPool.count { it.state == PlayerState.PLAYING }

    private fun pauseOldPlayers() {
        if (getCurrentPlayingVideoCount() > videoPlayingConcurrentMax) {
            var pauseCountLatch = getCurrentPlayingVideoCount() - videoPlayingConcurrentMax

            val playersSortedOldest = playerPool.sortedBy { it.latestUsedTimeMs }

            playersSortedOldest.forEach {
                if (pauseCountLatch == 0) return@forEach
                if (it.state == PlayerState.PLAYING) {
                    it.pause()
                    pauseCountLatch -= 1
                }
            }
        }
    }

    private fun playNewCandidate() {

        val adapter = (adapter as? PlayableAdapter<*>) ?: return
        val playable = adapter.currentList[firstCandidatePosition]

        val target: PlayableTarget = getPlayableTargetWithPosition(firstCandidatePosition)
        debugE(target)

        when (target.state) {
            TargetState.ATTACHED -> {
                debugE(TAG, "ATTACHED")
                target.player!!.play(playable)
            }
            TargetState.DETACHED -> {
                debugE(TAG, "DETACHED")
                val newPlayer = dequeueOldestPlayer()
                val oldTarget = newPlayer.target
                newPlayer.detach()

                newPlayer.attach(oldTarget, target)
                newPlayer.play(playable)
            }
        }

        pauseOldPlayers()
    }

    //endregion


    //region Lifecycle

    override fun onChildDetachedFromWindow(child: View) {
//        val target = getPlayableTargetWithView(child)
//        target.player?.detach()
    }

    //endregion

    //region Utils

    private fun getPlayableTargetWithView(view: View): PlayableTarget {
        return getPlayableTargetWithPosition(getChildLayoutPosition(view))
    }

    private fun getPlayableTargetWithPosition(position: Int): PlayableTarget {
        return findViewHolderForLayoutPosition(position) as PlayableTarget
    }

    @Suppress("UNCHECKED_CAST")
    private fun updatePlayerPriority() {
        playerQueueLock.acquire()
        playerQueue.clear()
        playerPool.forEach { playerQueue.offer(it as PlayablePlayer<PlayableTarget>) }
        playerQueueLock.release()
    }

    private fun dequeueOldestPlayer(): PlayablePlayer<PlayableTarget> {
        updatePlayerPriority()
        return playerQueue.peek()?.also { it.updateUsedTime() } ?: throw RuntimeException("WTF")
    }

    //endregion


}