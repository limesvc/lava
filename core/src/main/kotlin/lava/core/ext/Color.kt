package lava.core.ext

import androidx.annotation.IntRange

/**
 * Created by svc on 2021/01/09
 */

fun Long.percentAlpha(@IntRange(from = 0, to = 100) percent: Int): Int {
    val color = this.toInt()
    val origin = color.ushr(24)
    val result = percent / 100f * origin
    return result.toInt().shl(24).or(color.and(0xffffff))
}

fun Int.percentAlpha(@IntRange(from = 0, to = 100) percent: Int): Int {
    val origin = this.ushr(24)
    val result = percent / 100f * origin
    return result.toInt().shl(24).or(this.and(0xffffff))
}

fun Int.reviseAlpha(@IntRange(from = 0, to = 100) alpha: Int): Int {
    return (alpha / 100f * 0xff).toInt().shl(24).or(this.and(0xffffff))
}