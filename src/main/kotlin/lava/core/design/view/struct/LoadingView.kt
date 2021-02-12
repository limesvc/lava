package lava.core.design.view.struct

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import lava.core.util.LPUtil

/**
 * Created by svc on 2021/2/12
 */
class LoadingView : LoadingStructX() {
    private var layout: FrameLayout? = null


    override fun getView(context: Context): View {
        return layout ?: FrameLayout(context).apply {
            val progressDrawable = CircularProgressDrawable(context)
            val imageView = ImageView(context)
            imageView.setImageDrawable(progressDrawable)
            val frame = LPUtil.frame(36, 36)
            frame.gravity = Gravity.CENTER
            addView(imageView, frame)

            layout = this
        }

    }
}