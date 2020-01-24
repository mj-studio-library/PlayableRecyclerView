package happy.mjstudio.playablerecyclerview.target

import happy.mjstudio.playablerecyclerview.enum.TargetState
import happy.mjstudio.playablerecyclerview.player.PlayablePlayer

/**
 * Created by mj on 21, January, 2020
 */
interface PlayableTarget {
    var state: TargetState

    var player: PlayablePlayer<out PlayableTarget>?

    fun onAttached(player: PlayablePlayer<out PlayableTarget>) {
        this.player = player
        state = TargetState.ATTACHED
//        showThumbnail()
        showLoading()
    }

    fun onDetached() {
        this.player = null
        state = TargetState.DETACHED
        showThumbnail()
        hideLoading()
    }

    fun showThumbnail()

    fun hideThumbnail()

    fun showLoading()

    fun hideLoading()
}