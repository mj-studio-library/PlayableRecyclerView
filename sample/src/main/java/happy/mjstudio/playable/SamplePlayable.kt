package happy.mjstudio.playable

import android.os.Parcelable
import happy.mjstudio.playablerecyclerview.model.Playable
import kotlinx.android.parcel.Parcelize

/**
 * Created by mj on 21, January, 2020
 */
@Parcelize
data class SamplePlayable(
    override val videoUrl: String,
    override val thumbnailUrl: String? = null
) : Parcelable, Playable