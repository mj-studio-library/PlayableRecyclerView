package happy.mjstudio.playablerecyclerview.cache

import android.app.Application
import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer
import happy.mjstudio.playablerecyclerview.view.PlayableRecyclerView.Companion.DEFAULT_CACHE_MAX_SIZE_MB

/**
 * Created by mj on 29, January, 2020
 */
class PlayableCacheServer private constructor(context: Context, cacheSizeMb: Int) {

    private val proxyServer = HttpProxyCacheServer.Builder(context)
        .maxCacheSize(1024L * 1024 * cacheSizeMb) // 1GB
        .build()

    fun getCachedUrl(url: String) = (proxyServer.getProxyUrl(url) ?: url)

    companion object {

        private var cacheServer: PlayableCacheServer? = null

        fun getInstance(context: Application, cacheSizeMb: Int = DEFAULT_CACHE_MAX_SIZE_MB): PlayableCacheServer {
            return cacheServer ?: PlayableCacheServer(context, cacheSizeMb)
        }

    }
}