package happy.mjstudio.playable

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.playable.databinding.ActivityMainBinding
import happy.mjstudio.playablerecyclerview.enum.PlayerState

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val sampleDatas = listOf(
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.png"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/1579496605382_merged.mp4_1579496619388.mp4",
            "https://cdn.staging.beatflo.co/video/thumbnail.jpg_1579496619819.mp4"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.png"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.png"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/1579496605382_merged.mp4_1579496619388.mp4",
            "https://cdn.staging.beatflo.co/video/thumbnail.jpg_1579496619819.mp4"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.png"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.png"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/1579496605382_merged.mp4_1579496619388.mp4",
            "https://cdn.staging.beatflo.co/video/thumbnail.jpg_1579496619819.mp4"
        ),
        SamplePlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.png"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        mBinding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        mBinding.apply {
            with(recyclerView) {
                adapter = SampleAdapter { clickedPosition ->

                    when (manager.getPlaygingState(clickedPosition)) {
                        PlayerState.PLAYING -> manager.pausePlayable(clickedPosition)
                        else -> manager.playPlayable(clickedPosition)
                    }

                }.apply {
                    submitItems(sampleDatas)
                }

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        mBinding.shadow.isActivated = recyclerView.canScrollVertically(-1)
                    }
                })

                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val position = parent.getChildLayoutPosition(view)
                        if (position > 0)
                            outRect.top = 48
                    }
                })
            }

            with(title) {
                text = "𝐸𝓃𝒿𝑜𝓎 𝒫𝓁𝒶𝓎𝒶𝒷𝓁𝑒𝑅𝑒𝒸𝓎𝒸𝓁𝑒𝓇𝒱𝒾𝑒𝓌 - 𝑀𝒥𝒮𝓉𝓊𝒹𝒾𝑜"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.recyclerView.manager.resumeCurrentPlayable()
    }

    override fun onPause() {
        super.onPause()
        mBinding.recyclerView.manager.pauseAllPlayables()
    }

}