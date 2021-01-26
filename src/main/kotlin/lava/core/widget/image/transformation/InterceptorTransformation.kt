//package lava.core.widget.image.transformation
//
//import android.graphics.Bitmap
//import com.bumptech.glide.load.Key
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
//import vector.network.image.interceptor.Interceptor
//import java.security.MessageDigest
//
//class InterceptorTransformation(private val interceptors: List<Interceptor>) : BitmapTransformation() {
//
//    companion object {
//        private const val VERSION = 1
//        private const val ID = "vector.glide.transformation.interceptors.$VERSION"
//        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
//    }
//
//    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
//        var bitmap = toTransform
//        interceptors.forEach {
//            bitmap = it.process(bitmap)
//        }
//        return bitmap
//    }
//
//    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//        messageDigest.update(ID_BYTES)
//    }
//
//    override fun hashCode(): Int {
//        return ID.hashCode()
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return other is InterceptorTransformation
//    }
//}