package lava.core.design.view.struct

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import lava.core.R
import lava.core.ext.just
import lava.core.net.LoadingState
import lava.core.util.LPUtil
import lava.core.util.dp

/**
 * Created by svc on 2021/2/12
 */
class LoadingView : LoadingStructX() {
    private var color: Int = 0x00C9A7

    private var layout: FrameLayout? = null

    private val size = 36.dp()

    override fun getView(context: Context, host: StructHost): View {
        return layout ?: FrameLayout(context).apply {
            val progressBar = ProgressBar(context)
            progressBar.indeterminateDrawable?.just {
                colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
            }
            progressBar.progressDrawable?.just {
                colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
            }

            val frame = LPUtil.frame(size, size)
            frame.gravity = Gravity.CENTER
            addView(progressBar, frame)
            setBackgroundColor(ContextCompat.getColor(context, R.color.mask_gray))
            setOnClickListener { }

            layout = this
        }

    }

    override fun updateState(state: LoadingState) {
        layout?.isVisible = state == LoadingState.LOADING
    }
}