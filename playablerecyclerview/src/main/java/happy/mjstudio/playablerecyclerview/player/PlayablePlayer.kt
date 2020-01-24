package happy.mjstudio.playablerecyclerview.player

import androidx.annotation.CallSuper
import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.model.Playable
import happy.mjstudio.playablerecyclerview.target.PlayableTarget

/**
 * Created by mj on 20, January, 2020
 */
interface PlayablePlayer<Target : PlayableTarget> {

    var target: Target?

    @CallSuper
    fun attach(oldTarget: Target?, target: Target) {
        this.target = target
        target.onAttached(this)
    }

    @CallSuper
    fun detach() {
        target?.onDetached()
        target = null
    }

    val latestUsedTimeMs: Long
    val state: PlayerState

    @CallSuper
    fun play(playable: Playable) {
        updateUsedTime()
    }

    @CallSuper
    fun pause() {
        target?.hideLoading()
    }

    fun release()

    fun updateUsedTime()
}
