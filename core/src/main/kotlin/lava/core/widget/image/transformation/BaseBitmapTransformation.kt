package vector.network.image.glide.transformation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

/**
 * @author CaiXiang
 * @since 2019/5/17
 */
abstract class BaseBitmapTransformation : BitmapTransformation() {

    protected fun getAlphaSafeBitmap(
            pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
        val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
        if (safeConfig == maybeAlphaSafe.config) {
            return maybeAlphaSafe
        }

        val argbBitmap = pool.get(maybeAlphaSafe.width, maybeAlphaSafe.height, safeConfig)
        Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f /*left*/, 0f /*top*/, null /*paint*/)
        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap
    }

    private fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16 == inBitmap.config) { // NOPMD
                return Bitmap.Config.RGBA_F16
            }
        }
        return Bitmap.Config.ARGB_8888
    }

}