package happy.mjstudio.playablerecyclerview.util

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

/**
 * Created by mj on 21, January, 2020
 */
fun RecyclerView.attachSnapHelper(snapHelper: SnapHelper = PagerSnapHelper()) {
    snapHelper.attachToRecyclerView(this)
}