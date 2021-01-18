package lava.core.util

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout

const val MATCH = ViewGroup.LayoutParams.MATCH_PARENT

const val WRAP = ViewGroup.LayoutParams.MATCH_PARENT

object LPUtil {
    fun viewGroup(width: Int = MATCH, height: Int = MATCH) = ViewGroup.LayoutParams(width, height)

    fun linear(width: Int = MATCH, height: Int = MATCH) = LinearLayout.LayoutParams(width, height)

    fun relative(width: Int = MATCH, height: Int = MATCH) = RelativeLayout.LayoutParams(width, height)
}