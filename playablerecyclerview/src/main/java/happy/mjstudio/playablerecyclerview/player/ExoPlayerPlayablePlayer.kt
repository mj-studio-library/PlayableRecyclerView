package happy.mjstudio.playablerecyclerview.player

import android.content.Context
import androidx.core.net.toUri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.model.Playable
import happy.mjstudio.playablerecyclerview.target.ExoPlayerPlayableTarget

/**
 * Created by mj on 21, January, 2020
 */
class ExoPlayerPlayablePlayer(context: Context) : PlayablePlayer<ExoPlayerPlayableTarget> {

    override var latestUsedTimeMs: Long = System.currentTimeMillis()
    override var state = PlayerState.PAUSED

    private val dataSourceFactory = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(context,
        ExoPlayerPlayablePlayer::class.java.simpleName))

    private var lastHandledVideoUrl: String = ""

    val player = SimpleExoPlayer.Builder(context).build()

    init {
        player.repeatMode = Player.REPEAT_MODE_ONE
    }

    override var target: ExoPlayerPlayableTarget? = null

    override fun pause() {
        state = PlayerState.PAUSED

        player.playWhenReady = false
    }

    override fun play(playable: Playable) {
        super.play(playable)

        state = PlayerState.PLAYING

        //Resume Track
        if(playable.videoUrl == lastHandledVideoUrl) {
            //Do Noting
        }
        //Change Track
        else {
            val mediaSource = dataSourceFactory.createMediaSource(playable.videoUrl.toUri())
            player.prepare(mediaSource)
            player.seekTo(0L)
        }

        lastHandledVideoUrl = playable.videoUrl

        player.playWhenReady = true
    }

    override fun release() {
        player.release()
    }

    override fun updateUsedTime() {
        latestUsedTimeMs = System.currentTimeMillis()
    }

    override fun attach(oldTarget: ExoPlayerPlayableTarget?, target: ExoPlayerPlayableTarget) {
        super.attach(oldTarget,target)

        PlayerView.switchTargetView(player,oldTarget?.getPlayerView(),target.getPlayerView())
    }

    override fun detach() {
        super.detach()
        player.playWhenReady = false
    }
}