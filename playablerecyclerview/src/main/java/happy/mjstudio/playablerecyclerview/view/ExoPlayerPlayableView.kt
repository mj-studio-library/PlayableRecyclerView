package happy.mjstudio.playablerecyclerview.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.updateLayoutParams
import com.google.android.exoplayer2.ui.PlayerView
import com.victor.loading.rotate.RotateLoading
import happy.mjstudio.playablerecyclerview.common.loadUrlAsync
import kotlin.math.roundToInt

/**
 * Created by mj on 24, January, 2020
 */
class ExoPlayerPlayableView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : PlayerView(context, attrs), PlayableView {

    private val thumbnailView = ImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private val progressView = RotateLoading(context).apply {
        loadingColor = Color.WHITE
        isInvisible = true
        start()
    }

    init {
        addView(thumbnailView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        addView(progressView, LayoutParams(toPixel(80).roundToInt(), toPixel(80).roundToInt()).apply {
            gravity = Gravity.CENTER
        })
        setShowBuffering(SHOW_BUFFERING_NEVER)
    }

    private fun toPixel(dp: Int): Float = resources.displayMetrics.density * dp

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val newProgressViewLength = (w * 0.15f).roundToInt()
        progressView.updateLayoutParams<LayoutParams> {
            width = newProgressViewLength
            height = newProgressViewLength
        }
    }

    override fun setThumbnail(url: String?, placeholder: Drawable?) {
        thumbnailView.loadUrlAsync(url, placeholder)
    }

    override fun showThumbnail() {
        thumbnailView.isInvisible = false
    }

    override fun hideThumbnail() {
        thumbnailView.isInvisible = true
    }

    override fun showLoading() {
        progressView.isInvisible = false
    }

    override fun hideLoading() {
        progressView.isInvisible = true
    }
}
