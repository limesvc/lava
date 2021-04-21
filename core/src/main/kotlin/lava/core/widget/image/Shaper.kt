package lava.core.widget.image

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange

/**
 * 整形器
 *
 * @author yuansui
 */
interface Shaper

/**
 * 圆形
 * @param color 边框颜色
 * @param width 边框宽度
 */
data class CircleShaper(@IntRange(from = 0) val width: Int = 0, @ColorInt val color: Int = Color.TRANSPARENT) : Shaper

/**
 * 圆角
 * @param radius 半径
 */
data class CornerShaper(@IntRange(from = 0, to = 180) val radius: Int = 10) : Shaper

/**
 * 设置图片不同角度的圆角
 */
data class RoundCornersShaper(@IntRange(from = 0, to = 180) val leftTop: Int = 0,
                              @IntRange(from = 0, to = 180) val rightTop: Int = 10,
                              @IntRange(from = 0, to = 180) val rightBottom: Int = 10,
                              @IntRange(from = 0, to = 180) val leftBottom: Int = 10) : Shaper

/**
 * 不规则的形状  根据传进来的背景图计算  将resId图片的黑色区域弄成透明的，resId中要显示图片的区域是透明的
 */
data class IrregularShaper(@DrawableRes val resId: Int) : Shaper