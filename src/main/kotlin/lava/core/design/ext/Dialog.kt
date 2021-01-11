package lava.core.design.ext

import android.app.Dialog
import lava.core.bus.Bus

/**
 * Created by svc on 2021/01/11
 */
fun Dialog?.bindVisible(flag: Int, bus: Bus) {
    this?.also{
        bus.on<Boolean>(flag) {
            if (it) show() else dismiss()
        }
    }
}
