package happy.mjstudio.playable

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import happy.mjstudio.playable.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val sampleDatas = listOf(
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.png"
        ),
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/1579496605382_merged.mp4_1579496619388.mp4",
            "https://cdn.staging.beatflo.co/video/thumbnail.jpg_1579496619819.mp4"
        ),
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.png"
        ),
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.png"
        ),
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/1579496605382_merged.mp4_1579496619388.mp4",
            "https://cdn.staging.beatflo.co/video/thumbnail.jpg_1579496619819.mp4"
        ),
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_b_choreography.png"
        ),
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.mp4",
            "https://cdn.staging.beatflo.co/video/trippin_budy_part_c_choreography.png"
        ),
        CommonPlayable(
            "https://cdn.staging.beatflo.co/video/1579496605382_merged.mp4_1579496619388.mp4",
            "https://cdn.staging.beatflo.co/video/thumbnail.jpg_1579496619819.mp4"
        ),
        CommonPlayable(
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
                adapter = SampleAdapter().apply {
                    submitItems(sampleDatas)
                }
            }

            with(open) {
                setOnClickListener {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.recyclerView.manager.resumeCurrentPlayable()
    }

    override fun onPause() {
        super.onPause()
        mBinding.recyclerView.manager.pauseAllPlayable()
    }

}