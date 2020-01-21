package happy.mjstudio.playable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerView
import happy.mjstudio.playable.databinding.ItemPlayableBinding
import happy.mjstudio.playablerecyclerview.enum.TargetState
import happy.mjstudio.playablerecyclerview.model.Playable
import happy.mjstudio.playablerecyclerview.player.PlayablePlayer
import happy.mjstudio.playablerecyclerview.target.ExoPlayerPlayableTarget
import happy.mjstudio.playablerecyclerview.target.PlayableTarget
import happy.mjstudio.playablerecyclerview.view.PlayableAdapter

/**
 * Created by mj on 21, January, 2020
 */
class SampleAdapter : PlayableAdapter<SampleAdapter.SampleHolder>() {

    fun submitItems(items : List<Playable>) {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayableBinding.inflate(inflater, parent, false)

        return SampleHolder(binding)
    }


    override fun onBindViewHolder(holder: SampleHolder, position: Int){

    }


    inner class SampleHolder(private val binding: ItemPlayableBinding) : RecyclerView.ViewHolder(binding.root),
        ExoPlayerPlayableTarget {
        override fun getPlayerView(): PlayerView {
            return binding.playerView
        }

        override var player: PlayablePlayer<out PlayableTarget>? = null

        override var state: TargetState = TargetState.DETACHED
    }
}