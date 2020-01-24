package happy.mjstudio.playablerecyclerview.enum

import android.app.Application
import happy.mjstudio.playablerecyclerview.player.ExoPlayerPlayablePlayer

/**
 * Created by mj on 24, January, 2020
 */
enum class PlayableType(val rawValue: Int) {
    EXOPLAYER(0)

    ;

    fun generatePlayer(context: Application, loopType: LoopType) = when (this) {
        EXOPLAYER -> ExoPlayerPlayablePlayer(context, loopType)
    }

    companion object {
        fun parse(rawValue: Int) = values().first { it.rawValue == rawValue }
    }

}