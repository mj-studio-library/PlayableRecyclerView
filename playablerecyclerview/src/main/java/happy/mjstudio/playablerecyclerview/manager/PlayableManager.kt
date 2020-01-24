package happy.mjstudio.playablerecyclerview.manager

import happy.mjstudio.playablerecyclerview.enum.PlayerState
import happy.mjstudio.playablerecyclerview.player.PlayablePlayer

/**
 * Created by mj on 21, January, 2020
 */
interface PlayableManager {
    /**
     * Pause all Playables current playing
     */
    fun pauseAllPlayables()

    /**
     * Resume first candidate player
     */
    fun resumeCurrentPlayable()

    /**
     * call this method when data in RecyclerView is updated
     */
    fun updatePlayables()

    /**
     * Play Playable in specific position
     *
     * If play request was successful then return true
     */
    fun playPlayable(position: Int): Boolean

    /**
     * Pause Playable in specific position
     *
     * If pause request was successful then return true
     */
    fun pausePlayable(position: Int): Boolean

    /**
     * Get [PlayerState] in specific position
     */
    fun getPlaygingState(position: Int): PlayerState?

    /**
     * Seek playback of Playable in specific position
     */
    fun seekToPlayablePlayback(position: Int, ms: Long): Boolean

    /**
     * get [PlayablePlayer] in specific position
     */
    fun getPlayablePlayer(position: Int): PlayablePlayer?
}