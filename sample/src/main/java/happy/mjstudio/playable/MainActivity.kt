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

    private val sampleThumbs = listOf(
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerJoyrides.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerMeltdowns.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/Sintel.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/SubaruOutbackOnStreetAndDirt.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/TearsOfSteel.jpg"
    )

    private val sampleVideos = listOf(
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.jpg",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
    )

    private val sampleDatas = sampleVideos.indices.map {
        SamplePlayable(sampleVideos[it], sampleThumbs[it])
    }

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

                    /** Switch Play/Pause state */
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