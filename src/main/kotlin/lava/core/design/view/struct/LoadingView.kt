package lava.core.design.view.struct

import android.content.Context
import android.graphics.Paint
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import lava.core.R
import lava.core.net.LoadingState
import lava.core.util.LPUtil

/**
 * Created by svc on 2021/2/12
 */
class LoadingView : LoadingStructX() {
    private var color: Int = 0x00C9A7

    private var layout: FrameLayout? = null

    override fun getView(context: Context, host: StructHost): View {
        return layout ?: FrameLayout(context).apply {
            val progressDrawable = CircularProgressDrawable(context)
            // 设置边界
            progressDrawable.setBounds(0, 0, 5, 5)
            // 设置绘制进度弧长
            progressDrawable.setStartEndTrim(0f, 0.7f);
            // 设置样式
            progressDrawable.setStyle(CircularProgressDrawable.LARGE);
            // 设置环形的宽度
            progressDrawable.strokeWidth = 10f;
            // 设置环形的节点显示(Paint.Cap.ROUND即圆角)
            progressDrawable.strokeCap = Paint.Cap.ROUND;
            // 设置环形的半径(控制环形的尺寸)
            progressDrawable.centerRadius = 40f;
            // 设置背景颜色
            progressDrawable.backgroundColor = color
            // 设置循环颜色
            progressDrawable.setColorSchemeColors(color)
            // 设置选择次数
            progressDrawable.start()

            val imageView = ImageView(context)
            imageView.setImageDrawable(progressDrawable)
//            imageView.setImageResource(R.drawable.ic_launcher)
            val frame = LPUtil.frame()
            frame.gravity = Gravity.CENTER
            addView(imageView, frame)
            setBackgroundColor(ContextCompat.getColor(context, R.color.mask_gray))
//            setOnClickListener { }

            layout = this
        }

    }

    override fun updateState(state: LoadingState) {
        layout?.isVisible = state == LoadingState.LOADING
    }
}