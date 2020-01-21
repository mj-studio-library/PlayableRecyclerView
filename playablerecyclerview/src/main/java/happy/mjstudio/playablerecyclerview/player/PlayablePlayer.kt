package happy.mjstudio.playablerecyclerview.player

import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.model.Playable
import happy.mjstudio.playablerecyclerview.target.PlayableTarget

/**
 * Created by mj on 20, January, 2020
 */
interface PlayablePlayer<Target: PlayableTarget> {

    var target: Target?

    fun attach(oldTarget: Target?, target: Target) {
        this.target = target
        target.onAttached(this)
    }
    fun detach() {
        target?.onDetached()
        target = null
    }

    val latestUsedTimeMs: Long
    val state: PlayerState

    fun play(playable: Playable) {
        updateUsedTime()
    }
    fun pause()

    fun release()

    fun updateUsedTime()
}
