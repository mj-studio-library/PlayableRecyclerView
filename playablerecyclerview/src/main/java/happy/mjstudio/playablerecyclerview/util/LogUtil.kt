package happy.mjstudio.playablerecyclerview.util

import android.util.Log
import happy.mjstudio.playablerecyclerview.BuildConfig

/**
 * Created by mj on 21, January, 2020
 */
fun debugE(tag: String, message: Any?) {
    if (BuildConfig.DEBUG)
        Log.e(tag, "🧩" + message.toString() + "🧩")
}

fun debugE(message: Any?) {
    debugE("DEBUG", message)
}