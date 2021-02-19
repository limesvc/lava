package lava.core.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by svc on 2021/2/19
 */

fun RecyclerView.snapToTop(position: Int) {
    scrollToPosition(position)
    val mLayoutManager = layoutManager as? LinearLayoutManager
    mLayoutManager?.scrollToPositionWithOffset(position, 0)
}

fun RecyclerView.smoothSnapTo(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}