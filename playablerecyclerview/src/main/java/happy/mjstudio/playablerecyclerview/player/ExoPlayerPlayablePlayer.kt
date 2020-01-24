package happy.mjstudio.playablerecyclerview.player

import android.content.Context
import androidx.core.net.toUri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import happy.mjstudio.playablerecyclerview.enum.LoopType
import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.target.PlayableTarget

/**
 * Created by mj on 21, January, 2020
 */
class ExoPlayerPlayablePlayer(context: Context, loopType: LoopType) : PlayablePlayer {

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
                            if (state == PlayerState.PLAYING)
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

    @Suppress("ControlFlowWithEmptyBody")
    override fun play(videoUrl: String) {
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
        PlayerView.switchTargetView(player, oldTarget?.getPlayableView() as? PlayerView, target.getPlayableView() as? PlayerView)
    }

    override fun detach() {
        super.detach()
        player.stop()
    }
}