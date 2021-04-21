package lava.core.util

import android.util.DisplayMetrics
import lava.core.appContext


object UIUtil {
    private const val TARGET_WIDTH = 1080
    private const val TARGET_HEIGHT = 1920
    private const val TARGET_DENSITY = 3

    private val fitRatio: Float

    val deviceWidth: Int
    val deviceHeight: Int
    val density: Float

    init {
        val dm: DisplayMetrics = appContext.resources.displayMetrics
        deviceWidth = dm.widthPixels
        deviceHeight = dm.heightPixels
        density = dm.density
        fitRatio = deviceWidth * 1f / TARGET_WIDTH
    }

    fun dp(dp: Float): Int {
        return (dp * TARGET_DENSITY * fitRatio).toInt()
    }

    fun dp(dp: Int): Int {
        return (dp * TARGET_DENSITY * fitRatio).toInt()
    }
}

fun Int.dp(): Int {
    return UIUtil.dp(this)
}

fun Float.dp(): Int {
    return UIUtil.dp(this)
}