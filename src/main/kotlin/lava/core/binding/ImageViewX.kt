package lava.core.binding

import androidx.databinding.BindingAdapter
import lava.core.widget.image.ImageViewX

/**
 * Created by svc on 2021/2/12
 */

const val PREFIX_IMG = "android:image_"

const val IMG_URL = PREFIX_IMG.plus("url")

@BindingAdapter(IMG_URL)
fun bindImageViewX(view: ImageViewX, url: String) {
    view.url(url).load()
}