package lava.core.ext

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent

/**
 * Created by svc on 2021/4/22
 */

fun Intent.start(context: Context) {
    component?.className?.let {
        val clazz = Class.forName(it)
        if (Activity::class.java.isAssignableFrom(clazz)) {
            if (context !is Activity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(this)
        } else if (Service::class.java.isAssignableFrom(clazz)) {
            context.startService(this)
        }
    }
}