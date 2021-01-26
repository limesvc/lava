//package vector.network.image.glide.transformation
//
//import android.graphics.Bitmap
//import android.graphics.Paint
//import android.graphics.PorterDuff
//import android.graphics.PorterDuffXfermode
//import androidx.annotation.DrawableRes
//import androidx.core.graphics.applyCanvas
//import com.bumptech.glide.load.Key
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
//import vector.appContext
//import vector.ext.applyCanvas
//import vector.ext.resize
//import vector.util.BmpLoader
//import java.security.MessageDigest
//
//
///**
// * 不规则的图片形状
// *
// * @author CaiXiang
// * @since 2019/4/17
// */
//class IrregularCrop constructor(@DrawableRes val resId: Int) : BaseBitmapTransformation() {
//
//    companion object {
//        private const val VERSION = 1
//        private const val ID = "glide.transformations.IrregularCrop.$VERSION"
//        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return other is IrregularCrop
//    }
//
//    override fun hashCode(): Int {
//        return ID.hashCode()
//    }
//
//    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//        messageDigest.update(ID_BYTES)
//    }
//
//    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
//        // Alpha is required for this transformation.
//        val toTransform2 = getAlphaSafeBitmap(pool, toTransform)
//
//        val bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
//        bitmap.applyCanvas {
//            // 不規則边框的bitmap
//            val rsc = BmpLoader.loadVector(appContext, resId) ?: return@applyCanvas
//            val mask = rsc.resize(outWidth.toFloat(), outHeight.toFloat())
//            rsc.recycle()
//
//            drawBitmap(mask, 0f, 0f, null)
//
//            val paint = Paint().apply {
//                isAntiAlias = true          //设置抗锯齿
//                style = Paint.Style.FILL    //设置填充样式
//                isDither = true             //设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
//                isFilterBitmap = true       //加快显示速度，本设置项依赖于dither和xfermode的设置
//                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT) //取得mask之外task之中的bitmap
//            }
//            drawBitmap(toTransform, 0f, 0f, paint)
//        }
//
//        if (toTransform2 != toTransform) {
//            pool.put(toTransform2)
//        }
//        return bitmap
//    }
//
//}