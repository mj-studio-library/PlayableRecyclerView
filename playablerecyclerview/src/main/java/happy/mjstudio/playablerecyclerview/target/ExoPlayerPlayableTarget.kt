package happy.mjstudio.playablerecyclerview.target

import com.google.android.exoplayer2.ui.PlayerView

/**
 * Created by mj on 21, January, 2020
 */
interface ExoPlayerPlayableTarget: PlayableTarget {
    fun getPlayerView(): PlayerView
}