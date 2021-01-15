package lava.core.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context


/**
 * Created by svc on 2021/01/15
 */

object AppUtil {
    fun getAvailMem(context: Context): Long {
        val manager = context
            .getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(info)
        // 单位Byte
        return info.availMem
    }
}