package lava.core.widget.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * Created by svc on 2021/1/26
 */
interface ImageProvider {
    fun attach(v: ImageView)

    fun url(url: String?): ImageProvider

    fun uri(uri: Uri?): ImageProvider

    fun reserveUrl(reserveUrl: String?): ImageProvider

    fun thumbnail(url: String?): ImageProvider

    fun bytes(bytes: ByteArray?): ImageProvider

    fun bitmap(bitmap: Bitmap?): ImageProvider

    fun storage(path: String?): ImageProvider

    fun res(@DrawableRes id: Int): ImageProvider

    fun src(drawable: Drawable?): ImageProvider

    fun shaper(shaper: Shaper): ImageProvider

//    fun addInterceptor(interceptor: Interceptor): ImageProvider

    fun size(width: Int, height: Int): ImageProvider

    fun placeholder(@DrawableRes id: Int): ImageProvider

    fun placeholder(drawable: Drawable?): ImageProvider

    fun scaleType(type: ScaleType): ImageProvider

    fun fadeInEnabled(enabled: Boolean): ImageProvider

    fun onLoadReady(action: (width: Int, height: Int) -> Unit): ImageProvider

    fun onLoadError(action: (e: Exception?) -> Unit): ImageProvider

    fun asBitmap(): ImageProvider

    fun clear()

    fun load()
}