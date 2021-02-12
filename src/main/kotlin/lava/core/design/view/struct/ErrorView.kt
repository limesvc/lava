package lava.core.design.view.struct

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import lava.core.ext.inflater
import lava.core.util.LPUtil

/**
 * Created by svc on 2021/2/12
 */
class ErrorView: ErrorStructX() {
    private var layout: LinearLayout? = null
    private var layoutId: Int = 0

    override fun getView(context: Context): View {
        return layout ?: LinearLayout(context).apply {
            if (layoutId != 0) {
                inflater.inflate(layoutId, this, true)
            } else {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                val imageView = ImageView(context)
                val textView = TextView(context)

                val progressDrawable = CircularProgressDrawable(context)
                imageView.setImageDrawable(progressDrawable)
                addView(imageView, LPUtil.viewGroup(36, 36))
                textView.gravity = Gravity.CENTER
                textView.setTextColor(Color.BLACK)

                addView(textView)
            }

            layout = this
        }
    }

    fun setLayoutId(layout: Int) {
        layoutId = layout
    }
}