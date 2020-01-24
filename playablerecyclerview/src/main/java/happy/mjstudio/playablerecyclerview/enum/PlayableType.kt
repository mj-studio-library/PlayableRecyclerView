package happy.mjstudio.playablerecyclerview.enum

import android.content.Context
import happy.mjstudio.playablerecyclerview.player.ExoPlayerPlayablePlayer

/**
 * Created by mj on 24, January, 2020
 */
enum class PlayableType(val rawValue: Int) {
    EXOPLAYER(0)

    ;

    fun generatePlayer(context: Context) = when (this) {
        EXOPLAYER -> ExoPlayerPlayablePlayer(context)
    }

    companion object {
        fun parse(rawValue: Int) = values().first { it.rawValue == rawValue }
    }

}