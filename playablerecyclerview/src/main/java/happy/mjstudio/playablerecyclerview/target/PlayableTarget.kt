package happy.mjstudio.playablerecyclerview.target

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.playablerecyclerview.enum.TargetState
import happy.mjstudio.playablerecyclerview.player.PlayablePlayer
import happy.mjstudio.playablerecyclerview.view.PlayableView

/**
 * Created by mj on 21, January, 2020
 */
abstract class PlayableTarget<T>(view: View) : RecyclerView.ViewHolder(view) {
    var state: TargetState = TargetState.DETACHED

    var player: PlayablePlayer? = null

    abstract fun getPlayableView(): PlayableView

    @CallSuper
    fun onAttached(player: PlayablePlayer) {
        this.player = player
        state = TargetState.ATTACHED
        showLoading()
    }

    @CallSuper
    fun onDetached() {
        this.player = null
        state = TargetState.DETACHED
        showThumbnail()
        hideLoading()
    }

    @CallSuper
    fun showThumbnail() {
        getPlayableView().showThumbnail()
        onShowThumbnail()
    }

    @CallSuper
    fun hideThumbnail() {
        getPlayableView().hideThumbnail()
        onHideThumbnail()
    }

    @CallSuper
    fun showLoading() {
        getPlayableView().showLoading()
        onShowLoading()
    }

    @CallSuper
    fun hideLoading() {
        getPlayableView().hideLoading()
        onHideLoading()
    }

    abstract fun onShowThumbnail()
    abstract fun onHideThumbnail()
    abstract fun onShowLoading()
    abstract fun onHideLoading()

    abstract fun getVideoUrl(): String
    abstract fun getThumbnailUrl(): String?
}