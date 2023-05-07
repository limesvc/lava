package lava.core.util

import android.content.Context
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

    fun screenHeight(): Int {
        val dm: DisplayMetrics = appContext.resources.displayMetrics
        return dm.heightPixels
    }

    fun screenWidth(): Int {
        val dm: DisplayMetrics = appContext.resources.displayMetrics
        return dm.widthPixels
    }

    fun screenSize(): IntArray {
        val dm: DisplayMetrics = appContext.resources.displayMetrics
        return intArrayOf(dm.widthPixels, dm.heightPixels)
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = appContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = appContext.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}

fun Int.dp(): Int {
    return UIUtil.dp(this)
}

fun Float.dp(): Int {
    return UIUtil.dp(this)
}