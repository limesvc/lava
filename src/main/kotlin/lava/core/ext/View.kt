package lava.core.ext

import android.view.LayoutInflater
import android.view.View
import lava.core.R

val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun View?.singleClick(interval: Long = 500, block: (v: View) -> Unit) {
    this?.setOnClickListener {
        val lastTime = getTag(R.id.last_click_time) as? Long ?: 0L
        if (lastTime - interval >= System.currentTimeMillis()) {
            kotlin.runCatching {
                block(this)
            }.onFailure {
                it.printStackTrace()
            }
            setTag(R.id.last_click_time, System.currentTimeMillis())
        }
    }
}

fun View?.safeClick(block: (v: View) -> Unit){
    this?.setOnClickListener {
        kotlin.runCatching {
            block(this)
        }
    }
}