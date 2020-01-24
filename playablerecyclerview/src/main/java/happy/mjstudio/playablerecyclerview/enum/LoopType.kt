package happy.mjstudio.playablerecyclerview.enum

/**
 * Created by mj on 24, January, 2020
 */
enum class LoopType(val rawValue: Int) {
    LOOP(0),
    NONE(1)

    ;

    companion object {
        fun parse(rawValue: Int) = values().first { it.rawValue == rawValue }
    }
}