package happy.mjstudio.playablerecyclerview.view

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import happy.mjstudio.playablerecyclerview.target.PlayableTarget

/**
 * Created by mj on 21, January, 2020
 */
abstract class PlayableAdapter<VH : PlayableTarget>(DIFF: DiffUtil.ItemCallback<Any>? = null) :
    ListAdapter<Any, VH>(DIFF ?: PlayableAdapter.DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)

        holder.player?.pause()
        holder.player?.seekTo(0)
        holder.showThumbnail()
        holder.hideLoading()
    }
}

