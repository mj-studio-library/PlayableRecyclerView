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
    override val thumbnailUrl: String? = null,
    val profileImageUrl: String = "https://avatars3.githubusercontent.com/u/33388801?s=460&v=4",
    val title: String = listOf(
        "It is very functional day",
        "I am happy to introduce my PlayableRecyclerView",
        "Revolutional Player List Library",
        "No Memory Leak",
        "Fast, Robust!"
    ).random(),
    val location: String = listOf(
        "Gimpo",
        "Seoul",
        "Gwangju",
        "Boosan",
        "Jeju",
        "Stanford"
    ).random(),
    val description: String = listOf(
        "This is MJStargram's description. It need to be long but we can't",
        "Who want this library"
    ).random()
) : Parcelable, Playable