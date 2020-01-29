package happy.mjstudio.playablerecyclerview.player

import android.app.Application
import android.content.Context
import androidx.core.net.toUri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import happy.mjstudio.playablerecyclerview.cache.PlayableCacheServer
import happy.mjstudio.playablerecyclerview.enum.LoopType
import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.target.PlayableTarget

/**
 * Created by mj on 21, January, 2020
 */
class ExoPlayerPlayablePlayer(context: Context, loopType: LoopType, private val showLoading: Boolean, private val useCache: Boolean) :
    PlayablePlayer {

    /** For Disk Cache */
    private val cacheServer: PlayableCacheServer = PlayableCacheServer.getInstance(context.applicationContext as Application)

    override var latestUsedTimeMs: Long = System.currentTimeMillis()
    override var _state: PlayerState = PlayerState.PAUSED

    private val dataSourceFactory = ProgressiveMediaSource.Factory(
        DefaultDataSourceFactory(
            context,
            ExoPlayerPlayablePlayer::class.java.simpleName
        )
    )

    private val player = SimpleExoPlayer.Builder(context).build()

    private var lastHandledVideoUrl: String = ""

    init {
        initPlayer(loopType)
    }

    private fun initPlayer(loopType: LoopType) {
        player.run {

            playWhenReady = false

            repeatMode = when (loopType) {
                LoopType.LOOP -> Player.REPEAT_MODE_ONE
                LoopType.NONE -> Player.REPEAT_MODE_OFF
            }

            addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {

                        Player.STATE_BUFFERING -> {
                            if (state == PlayerState.PLAYING && showLoading)
                                target?.showLoading()
                        }

                        Player.STATE_READY -> {

                            if (playWhenReady) {
                                target?.hideLoading()
                                target?.hideThumbnail()
                            }

                        }

                        Player.STATE_ENDED -> {

                        }

                        Player.STATE_IDLE -> {

                        }
                    }
                }
            })
        }
    }

    override var target: PlayableTarget<*>? = null

    override fun pause() {
        super.pause()

        player.playWhenReady = false
    }

    @Suppress("ControlFlowWithEmptyBody", "NAME_SHADOWING")
    override fun play(videoUrl: String) {
        val videoUrl = if (useCache) cacheServer.getCachedUrl(videoUrl) else videoUrl

        super.play(videoUrl)

        //Resume Track
        if (videoUrl == lastHandledVideoUrl) {
            //Do Noting
        }
        //Change Track
        else {
            val mediaSource = dataSourceFactory.createMediaSource(videoUrl.toUri())
            player.prepare(mediaSource)
            player.seekTo(0L)
        }

        lastHandledVideoUrl = videoUrl


        player.playWhenReady = true
    }

    override fun seekTo(ms: Long) {
        player.seekTo(ms)
    }

    override fun release() {
        player.release()
    }

    override fun updateUsedTime() {
        latestUsedTimeMs = System.currentTimeMillis()
    }

    override fun attach(oldTarget: PlayableTarget<*>?, target: PlayableTarget<*>) {
        super.attach(oldTarget, target)
        switchView(oldTarget?.getPlayableView() as? PlayerView, target.getPlayableView() as? PlayerView)
    }

    fun switchView(oldView: PlayerView?, view: PlayerView?) {
        PlayerView.switchTargetView(player, oldView, view)
    }

    override fun detach() {
        super.detach()
        player.stop()
    }
}