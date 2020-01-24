package happy.mjstudio.playablerecyclerview.view

import android.graphics.drawable.Drawable

/**
 * Created by mj on 24, January, 2020
 */
interface PlayableView {
    /**
     * Set thumbnail of Playable
     *
     * This is called usually when RecyclerView#onBindViewHolder is called
     */
    fun setThumbnail(url: String?, placeholder: Drawable? = null)

    fun showThumbnail()

    fun hideThumbnail()

    fun showLoading()

    fun hideLoading()
}