package lava.core.ext

import android.view.animation.Animation
import lava.core.adopt.AnimationCallbacks

/**
 * Created by svc on 2021/6/24
 */
typealias AnimLsn = (Animation?) -> Unit

fun Animation.onStart(block: AnimLsn) {
    setAnimationListener(object : AnimationCallbacks {
        override fun onAnimationStart(animation: Animation?) {
            block.invoke(animation)
        }
    })
}

fun Animation.onRepeat(block: AnimLsn) {
    setAnimationListener(object : AnimationCallbacks {
        override fun onAnimationRepeat(animation: Animation?) {
            block.invoke(animation)
        }
    })
}

fun Animation.onEnd(block: AnimLsn) {
    setAnimationListener(object : AnimationCallbacks {
        override fun onAnimationEnd(animation: Animation?) {
            block.invoke(animation)
        }
    })
}

fun Animation.setListener(onStart: AnimLsn = {}, onRepeat: AnimLsn = {}, onEnd: AnimLsn = {}) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = onStart(animation)

        override fun onAnimationEnd(animation: Animation?) = onRepeat(animation)

        override fun onAnimationRepeat(animation: Animation?) = onEnd(animation)
    })
}