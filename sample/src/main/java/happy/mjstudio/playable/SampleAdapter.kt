package happy.mjstudio.playable

import android.view.LayoutInflater
import android.view.ViewGroup
import happy.mjstudio.playable.databinding.ItemPlayableBinding
import happy.mjstudio.playablerecyclerview.model.Playable
import happy.mjstudio.playablerecyclerview.target.PlayableTarget
import happy.mjstudio.playablerecyclerview.view.PlayableAdapter
import happy.mjstudio.playablerecyclerview.view.PlayableView

/**
 * Created by mj on 21, January, 2020
 */
class SampleAdapter : PlayableAdapter<SampleAdapter.SampleHolder>() {

    fun submitItems(items: List<Playable>) {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayableBinding.inflate(inflater, parent, false)

        return SampleHolder(binding)
    }

    override fun onBindViewHolder(holder: SampleHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class SampleHolder(private val binding: ItemPlayableBinding) : PlayableTarget(binding.root) {

        fun bind(item: Playable) {
            binding.playerView.setThumbnail((item as? SamplePlayable)?.thumbnailUrl)
        }

        override fun getPlayableView(): PlayableView {
            return binding.playerView
        }

        override fun onShowThumbnail() {
        }

        override fun onHideThumbnail() {
        }

        override fun onShowLoading() {
        }

        override fun onHideLoading() {
        }
    }
}