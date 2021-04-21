package vector.network.image.glide.transformation

import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest


/**
 * 说明：实现圆角裁剪，可对不同角度设置
 * @author: HZM
 * @Date: 2020/5/25
 */
class CornerTransform constructor(private val leftTopRadius: Int = 0, private val rightTopRadius: Int = 0,
                                  private val rightBottomRadius: Int = 0, private val leftBottomRadius: Int = 0)
    : BitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID = "vector.glide.transformation.CornerTransform.$VERSION"
        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val source = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        val result = pool[source.width, source.height, Bitmap.Config.ARGB_8888]
        result.setHasAlpha(true)

        val canvas = Canvas(result)
        val paint = Paint()
        paint.isAntiAlias = true
        //必须传入source，需要对已缩放的图片进行裁剪
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val rectF = RectF(0f, 0f, result.width.toFloat(), result.height.toFloat())
        val path = Path()
        val radius = floatArrayOf(
                leftTopRadius.toFloat(), leftTopRadius.toFloat(),
                rightTopRadius.toFloat(), rightTopRadius.toFloat(),
                rightBottomRadius.toFloat(), rightBottomRadius.toFloat(),
                leftBottomRadius.toFloat(), leftBottomRadius.toFloat()
        )

        path.addRoundRect(rectF, radius, Path.Direction.CW)
        canvas.drawPath(path, paint)
        //另外一种写法
       /* //左上角
        canvas.save()
        canvas.clipRect(0f, 0f, canvas.width / 2f, canvas.height / 2f)
        canvas.drawRoundRect(rectF, leftTopRadius.toFloat(), leftTopRadius.toFloat(), paint)
        canvas.restore()

        //右上角
        canvas.save()
        canvas.clipRect(canvas.width / 2f, 0f, canvas.width.toFloat(), canvas.height / 2f)
        canvas.drawRoundRect(rectF, rightTopRadius.toFloat(), rightTopRadius.toFloat(), paint)
        canvas.restore()

        //右下角
        canvas.save()
        canvas.clipRect(canvas.width / 2f, canvas.height / 2f, canvas.width.toFloat(), canvas.height.toFloat())
        canvas.drawRoundRect(rectF, rightBottomRadius.toFloat(), rightBottomRadius.toFloat(), paint)
        canvas.restore()

        //左下角
        canvas.save()
        canvas.clipRect(0f, canvas.height / 2f, canvas.width / 2f, canvas.height.toFloat())
        canvas.drawRoundRect(rectF, leftBottomRadius.toFloat(), leftBottomRadius.toFloat(), paint)
        canvas.restore()*/

        canvas.setBitmap(null)

        if (source != toTransform) {
            pool.put(source)
        }

        return result
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass == other?.javaClass -> true
            other is CornerTransform -> {
                other.leftTopRadius == leftTopRadius && other.rightTopRadius == rightTopRadius &&
                        other.leftBottomRadius == leftBottomRadius && other.rightBottomRadius == rightBottomRadius
            }
            else -> false
        }
    }

    private fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            return if (Bitmap.Config.RGBA_F16 == inBitmap.config) { // NOPMD
                Bitmap.Config.RGBA_F16
            }else {
                Bitmap.Config.ARGB_8888
            }
        }
        return Bitmap.Config.ARGB_8888
    }


}