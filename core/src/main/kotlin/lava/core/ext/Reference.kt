package lava.core.ext

import java.lang.ref.WeakReference

/**
 * Created by wuxi on 2021/7/8
 */

fun <T: Any> T?.toWeak(): WeakReference<T> {
    return WeakReference(this)
}