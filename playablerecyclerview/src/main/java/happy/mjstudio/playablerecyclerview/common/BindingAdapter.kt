package happy.mjstudio.playablerecyclerview.common

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by mj on 24, January, 2020
 */
fun ImageView.loadUrlAsync(url: String?, placeholder: Drawable? = null) {
    if (url == null) {
        Glide.with(this).load(placeholder).into(this)
    } else {
        Glide.with(this).load(url)
            .apply {
                if (placeholder != null) placeholder(placeholder)
            }
            .into(this)
    }
}