package happy.mjstudio.playable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import happy.mjstudio.playable.databinding.ActivityDetailBinding

/**
 * Created by mj on 25, January, 2020
 */
class DetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDetailBinding

    private val playerTransitionName: String by lazy { intent?.getStringExtra(ARG_PLAYER_TRANSITION_NAME) ?: "" }

//    private val playable: SamplePlayable by lazy { intent?.getParcelableExtra(ARG_PLAYABLE) as SamplePlayable }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        mBinding.lifecycleOwner = this
    }


    companion object {
        //        private const val ARG_PLAYER = "ARG_PLAYER"
        private const val ARG_PLAYER_TRANSITION_NAME = "ARG_PLAYER_TRANSITION_NAME"
//        private const val ARG_PLAYABLE = "ARG_PLAYABLE"
//
//        fun newIntent(context: Context, playerTransitionName: String, playable: SamplePlayable): Intent {
//            return Intent(context, DetailActivity::class.java).apply {
//                putExtra(ARG_PLAYER_TRANSITION_NAME, playerTransitionName)
//                putExtra(ARG_PLAYABLE, playable)
//            }
//        }
    }
}