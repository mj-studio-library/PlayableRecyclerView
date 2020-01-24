package happy.mjstudio.playable

import android.view.LayoutInflater
import android.view.ViewGroup
import happy.mjstudio.playable.databinding.ItemPlayableBinding
import happy.mjstudio.playablerecyclerview.target.PlayableTarget
import happy.mjstudio.playablerecyclerview.view.PlayableAdapter
import happy.mjstudio.playablerecyclerview.view.PlayableView

/**
 * Created by mj on 21, January, 2020
 */
class SampleAdapter(
    private val onItemClick: (Int) -> Unit
) : PlayableAdapter<SampleAdapter.SampleHolder>() {

    fun submitItems(items: List<SamplePlayable>) {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayableBinding.inflate(inflater, parent, false)

        return SampleHolder(binding)
    }

    override fun onBindViewHolder(holder: SampleHolder, position: Int) {
        holder.bind(currentList[position] as SamplePlayable)
    }

    inner class SampleHolder(private val binding: ItemPlayableBinding) : PlayableTarget(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(layoutPosition)
            }
        }

        fun bind(item: SamplePlayable) {
            binding.playerView.setThumbnail((item as? SamplePlayable)?.thumbnailUrl)
            binding.item = item
            binding.executePendingBindings()
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

        override fun getVideoUrl(): String {
            return (currentList[layoutPosition] as SamplePlayable).videoUrl
        }

        override fun getThumbnailUrl(): String? {
            return (currentList[layoutPosition] as SamplePlayable).thumbnailUrl
        }
    }
}