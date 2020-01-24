package happy.mjstudio.playablerecyclerview.player

import androidx.annotation.CallSuper
import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.target.PlayableTarget

/**
 * Created by mj on 20, January, 2020
 */
interface PlayablePlayer {

    var target: PlayableTarget?

    @CallSuper
    fun attach(oldTarget: PlayableTarget?, target: PlayableTarget) {
        this.target = target
        target.onAttached(this)
    }

    @CallSuper
    fun detach() {
        target?.onDetached()
        target = null
    }

    val latestUsedTimeMs: Long
    var _state: PlayerState
    val state: PlayerState get() = _state

    @CallSuper
    fun play(videoUrl: String) {
        _state = PlayerState.PLAYING
        updateUsedTime()
    }

    @CallSuper
    fun pause() {
        _state = PlayerState.PAUSED
        target?.hideLoading()
    }

    fun seekTo(ms: Long)

    fun release()

    fun updateUsedTime()
}
