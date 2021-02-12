package lava.core.util

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

const val MATCH = ViewGroup.LayoutParams.MATCH_PARENT

const val WRAP = ViewGroup.LayoutParams.MATCH_PARENT

object LPUtil {
    fun viewGroup(width: Int = MATCH, height: Int = MATCH) = ViewGroup.LayoutParams(width, height)

    fun linear(width: Int = MATCH, height: Int = MATCH) = LinearLayout.LayoutParams(width, height)

    fun relative(width: Int = MATCH, height: Int = MATCH) =
        RelativeLayout.LayoutParams(width, height)

    fun frame(width: Int = MATCH, height: Int = MATCH) = FrameLayout.LayoutParams(width, height)

    fun constraint(width: Int = MATCH, height: Int = MATCH) =
        ConstraintLayout.LayoutParams(width, height)
}