package lava.core.widget.image

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class ImageViewX @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), ImageProvider by GlideProvider() {
    init {
        attach(this)
    }
}