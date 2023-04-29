package lava.core.adopt

import android.view.animation.Animation

/**
 * Created by svc on 2021/6/24
 */
interface AnimationCallbacks : Animation.AnimationListener {

    override fun onAnimationStart(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) {}

    override fun onAnimationRepeat(animation: Animation?) {}
}