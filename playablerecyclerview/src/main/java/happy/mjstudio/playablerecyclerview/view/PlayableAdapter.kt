package happy.mjstudio.playablerecyclerview.view

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.playablerecyclerview.model.Playable

/**
 * Created by mj on 21, January, 2020
 */

abstract class PlayableAdapter<VH : RecyclerView.ViewHolder>() : ListAdapter<Playable, VH>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Playable>() {
            override fun areItemsTheSame(oldItem: Playable, newItem: Playable): Boolean {
                return oldItem.videoUrl == newItem.videoUrl
            }

            override fun areContentsTheSame(oldItem: Playable, newItem: Playable): Boolean {
                return oldItem == newItem
            }
        }
    }
}

