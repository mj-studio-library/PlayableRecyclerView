package happy.mjstudio.playable

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by mj on 21, January, 2020
 */
@Parcelize
data class SamplePlayable(
    val videoUrl: String,
    val thumbnailUrl: String? = null,
    val profileImageUrl: String = "https://avatars3.githubusercontent.com/u/33388801?s=460&v=4",
    val title: String = listOf(
        "It is very functional day",
        "I am happy to introduce my PlayableRecyclerView",
        "Revolutional Player List Library",
        "No Memory Leak",
        "Fast, Robust!",
        "Android hasn't native feature show list of videos. Believe me. Use PlayableRecyclerView",
        "This is my third Android library"
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
        "This is MJStargram's description. It need to be long but we can't\nWho wants this library\nThis is Veeeery Robust and Absolutely Fast\uD83E\uDDE9",
        "Who wants this library? The architecture of this library is not hard to read.\nI will create docs for this.🔮",
        "Great ideas do not rust. ⚔️",
        "This is just an alpha pre-release.\nWait me ⭕️",
        "Magic with Kotlin ⚓️"
    ).let { "${it.random()}\n${it.random()}" }
) : Parcelable