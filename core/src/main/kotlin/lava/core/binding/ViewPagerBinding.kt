package lava.core.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

const val PREFIX_VP = "android:vp_"
const val VP_ADAPTER = PREFIX_VP.plus("adapter")

@BindingAdapter(value = [VP_ADAPTER], requireAll = false)
fun bindVP(viewPager: ViewPager, adapter: PagerAdapter?) {
    viewPager.adapter = adapter
    viewPager.offscreenPageLimit = 3
}