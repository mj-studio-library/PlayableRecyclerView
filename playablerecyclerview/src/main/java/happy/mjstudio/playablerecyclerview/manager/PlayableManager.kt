package happy.mjstudio.playablerecyclerview.manager

import happy.mjstudio.playablerecyclerview.model.Playable

/**
 * Created by mj on 21, January, 2020
 */
interface PlayableManager {
    /**
     * Pause all [Playable] current playing
     */
    fun pauseAllPlayable()

    /**
     * Resume first candidate player
     */
    fun resumeCurrentPlayable()

    /**
     * call this method when data in RecyclerView is updated
     */
    fun updatePlayables()
}