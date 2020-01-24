package happy.mjstudio.playablerecyclerview.view

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import happy.mjstudio.playablerecyclerview.target.PlayableTarget

/**
 * Created by mj on 21, January, 2020
 */
abstract class PlayableAdapter<T, VH : PlayableTarget<T>>(DIFF: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(DIFF) {

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)

        holder.player?.pause()
        holder.player?.seekTo(0)
        holder.showThumbnail()
        holder.hideLoading()
    }
}

