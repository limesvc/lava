package lava.core.design.ext

import lava.core.design.viewmodel.ViewModelX

/**
 * Created by svc on 2021/01/11
 */

fun ViewModelX.show(flag: Int) {
    postUI(flag, true)
}