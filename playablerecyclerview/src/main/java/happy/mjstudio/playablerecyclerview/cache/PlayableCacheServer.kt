package happy.mjstudio.playablerecyclerview.cache

import android.app.Application
import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer

/**
 * Created by mj on 29, January, 2020
 */
class PlayableCacheServer private constructor(context: Context) {

    private val proxyServer = HttpProxyCacheServer.Builder(context)
        .maxCacheSize(2L * 1024 * 1024 * 1024) // 2GB
        .build()

    init {
//        proxyServer.registerCacheListener({ file, url, percentAvailable ->
//            debugE("Cached", "${file.path} $url $percentAvailable")
//        }, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    }

    fun getCachedUrl(url: String) = proxyServer.getProxyUrl(url, false) ?: url

    companion object {

        private var cacheServer: PlayableCacheServer? = null

        fun getInstance(context: Application): PlayableCacheServer {
            return cacheServer ?: PlayableCacheServer(context)
        }

    }
}