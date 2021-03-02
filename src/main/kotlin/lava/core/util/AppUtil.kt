package lava.core.util

/**
 * Created by svc on 2021/01/15
 */

object AppUtil {
    fun getAvailMem(): Long {
        System.gc()
        val r = Runtime.getRuntime()
        return r.maxMemory() - (r.totalMemory() - r.freeMemory())
    }
}