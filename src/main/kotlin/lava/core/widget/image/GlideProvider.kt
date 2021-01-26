package lava.core.widget.image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import lava.core.ext.doOnNotZero
import lava.core.net.glide.GlideApp
import lava.core.net.glide.GlideRequest
import lava.core.widget.image.transformation.CircleCrop
import vector.network.image.glide.transformation.CornerTransform

/**
 * Created by wuxi on 2021/1/26
 */
enum class ScaleType {
    CENTER_CROP,
    FIT_CENTER,
    CENTER_INSIDE,
}

class GlideProvider : ImageProvider {
    private lateinit var iv: ImageView

    private var url: String? = null
    private var uri: Uri? = null
    private var src: Drawable? = null
    private var reserveUrl: String? = null
    private var thumbnail: String? = null
    private var bytes: ByteArray? = null
    private var bitmap: Bitmap? = null
    private var path: String? = null
    @DrawableRes
    private var res: Int = 0
    private var shaper: Shaper? = null
//    private var interceptors: MutableList<Interceptor>? = null
    private var width = 0
    private var height = 0
    private var scaleType: ScaleType = ScaleType.CENTER_CROP
    private var placeholder = 0
    private var placeholderDrawable: Drawable? = null
    private var fadeIn = true
    private var asBitmap = false
    private var onLoadReady: ((width: Int, height: Int) -> Unit)? = null
    private var onLoadError: ((e: Exception?) -> Unit)? = null

    override fun attach(v: ImageView) {
        iv = v
    }

    override fun url(url: String?): ImageProvider {
        this.url = url
        this.uri = null
        this.bytes = null
        this.path = null
        this.res = 0
        this.src = null
        this.bitmap = null
        return this
    }

    override fun uri(uri: Uri?): ImageProvider {
        this.uri = uri
        this.url = null
        this.bytes = null
        this.path = null
        this.res = 0
        this.src = null
        this.bitmap = null
        return this
    }

    override fun reserveUrl(reserveUrl: String?): ImageProvider {
        this.reserveUrl = reserveUrl
        return this
    }

    override fun thumbnail(url: String?): ImageProvider {
        this.thumbnail = url
        return this
    }

    override fun bitmap(bitmap: Bitmap?): ImageProvider {
        this.bitmap = bitmap
        this.uri = null
        this.url = null
        this.bytes = null
        this.path = null
        this.res = 0
        this.src = null
        return this
    }

    override fun storage(path: String?): ImageProvider {
        this.path = path
        this.uri = null
        this.bytes = null
        this.res = 0
        this.url = null
        this.bitmap = null
        this.src = null
        return this
    }

    override fun res(@DrawableRes id: Int): ImageProvider {
        this.res = id
        this.uri = null
        this.bytes = null
        this.path = null
        this.url = null
        this.bitmap = null
        this.src = null
        return this
    }

    override fun src(drawable: Drawable?): ImageProvider {
        this.res = 0
        this.uri = null
        this.bytes = null
        this.path = null
        this.url = null
        this.bitmap = null
        this.src = drawable
        return this
    }

    override fun bytes(bytes: ByteArray?): ImageProvider {
        this.bytes = bytes
        this.uri = null
        this.res = 0
        this.path = null
        this.url = null
        this.bitmap = null
        this.src = null
        return this
    }

    override fun shaper(shaper: Shaper): ImageProvider {
        this.shaper = shaper
        return this
    }

//    override fun addInterceptor(interceptor: Interceptor): ImageProvider {
//        if (interceptors == null) interceptors = mutableListOf()
//        interceptors?.add(interceptor)
//        return this
//    }

    override fun size(width: Int, height: Int): ImageProvider {
        this.width = width
        this.height = height
        return this
    }

    override fun placeholder(id: Int): ImageProvider {
        if (id != 0) placeholder = id
        return this
    }

    override fun placeholder(drawable: Drawable?): ImageProvider {
        placeholderDrawable = drawable
        return this
    }

    override fun scaleType(type: ScaleType): ImageProvider {
        scaleType = type
        return this
    }

    override fun fadeInEnabled(enabled: Boolean): ImageProvider {
        fadeIn = enabled
        return this
    }

    override fun onLoadReady(action: (width: Int, height: Int) -> Unit): ImageProvider {
        onLoadReady = action
        return this
    }

    override fun onLoadError(action: (e: Exception?) -> Unit): ImageProvider {
        onLoadError = action
        return this
    }

    override fun asBitmap(): ImageProvider {
        asBitmap = true
        return this
    }

    override fun clear() {
        if (checkNotNull(iv.context)) {
            GlideApp.with(iv).clear(iv)
        }
    }

    override fun load() {
        if (asBitmap) {
            loadBitmap()
        } else {
            loadDrawable()
        }
    }

    /**
     * 检查context是否已销毁
     */
    private fun checkNotNull(context: Context?): Boolean {
        return if (context == null) {
            false
        }else {
            when (context) {
                is FragmentActivity -> !context.isDestroyed
                is ContextThemeWrapper -> checkNotNull(context.baseContext)
                else -> true
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadBitmap() {
        BitmapLoader(GlideApp.with(iv).asBitmap()).load()
    }

    @SuppressLint("CheckResult")
    private fun loadDrawable() {
        DrawableLoader(GlideApp.with(iv).asDrawable()).load()
    }

    abstract inner class GlideLoader<T>(val req: GlideRequest<T>) {

        abstract fun setFadeIn()
        abstract fun setThumbnail()
        abstract fun onError(requestListener: RequestListener<T>?)

        @SuppressLint("CheckResult")
        fun load() {
            when {
                res != 0 -> {
                    req.load(res)
                }
                else -> {
                    val realUri: Any? = when {
                        !url.isNullOrEmpty() -> url
                        uri != null -> uri
                        !path.isNullOrEmpty() -> path
                        bytes?.isNotEmpty() ?: false -> bytes
                        bitmap != null -> bitmap
                        src != null -> src
                        else -> null
                    }
                    req.load(realUri)
                }
            }

            if (fadeIn) setFadeIn()

            if (thumbnail != null) setThumbnail()

            when {
                placeholder > 0 -> req.placeholder(placeholder)
                placeholderDrawable != null -> req.placeholder(placeholderDrawable)
            }

            doOnNotZero(width, height) { t1, t2 ->
                req.override(t1, t2)
            }

            var requestListener: RequestListener<T>? = null
            if (onLoadError != null || onLoadReady != null) {
                requestListener = object : RequestListener<T> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<T>?, isFirstResource: Boolean): Boolean {
                        onLoadError?.invoke(e)
                        return false // Allow calling onLoadError on the Target.
                    }

                    override fun onResourceReady(resource: T, model: Any?, target: Target<T>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        var w = 0
                        var h = 0
                        when (resource) {
                            is Bitmap -> {
                                w = resource.width
                                h = resource.height
                            }
                            is Drawable -> {
                                w = resource.intrinsicWidth
                                h = resource.intrinsicHeight
                            }
                        }

                        onLoadReady?.invoke(w, h)

                        return false // Allow calling onResourceReady on the Target.
                    }
                }
                req.listener(requestListener)
            }
            if (reserveUrl != null) onError(requestListener)

            setShaper()

            req.into(iv)
        }

        private fun setShaper() {
            val transformations = mutableListOf<BitmapTransformation>()

//            interceptors?.let {
//                transformations.add(InterceptorTransformation(it))
//            }

            val scaleTypeEnabled = shaper == null || shaper is CornerShaper // scaleType是否生效
            if (scaleTypeEnabled) {
                when (scaleType) {
                    ScaleType.CENTER_CROP -> transformations.add(CenterCrop())
                    ScaleType.FIT_CENTER -> transformations.add(FitCenter())
                    ScaleType.CENTER_INSIDE -> transformations.add(CenterInside())
                }
            }

            when (shaper) {
                is CircleShaper -> {
                    val cs = shaper as CircleShaper
                    transformations.add(CircleCrop(cs.width.toFloat(), cs.color))
                }
                is CornerShaper -> {
                    val cs = shaper as CornerShaper
                    transformations.add(RoundedCorners(cs.radius))
                }
//                is IrregularShaper -> {
//                    val cs = shaper as IrregularShaper
//                    transformations.add(IrregularCrop(cs.resId))
//                }
                is RoundCornersShaper -> {
                    val cs = shaper as RoundCornersShaper
                    transformations.add(CornerTransform(cs.leftTop, cs.rightTop, cs.rightBottom, cs.leftBottom))
                }
            }

            if (transformations.isNotEmpty())
                req.transform(MultiTransformation(transformations))
        }
    }

    @SuppressLint("CheckResult")
    inner class DrawableLoader(req: GlideRequest<Drawable>) : GlideLoader<Drawable>(req) {
        override fun setFadeIn() {
            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            req.transition(DrawableTransitionOptions.withCrossFade(factory))
        }

        override fun setThumbnail() {
            req.thumbnail(GlideApp.with(iv).load(thumbnail))
        }

        override fun onError(requestListener: RequestListener<Drawable>?) {
            req.error(GlideApp.with(iv).load(reserveUrl).listener(requestListener))
        }
    }

    @SuppressLint("CheckResult")
    inner class BitmapLoader(req: GlideRequest<Bitmap>) : GlideLoader<Bitmap>(req) {
        override fun setFadeIn() {
            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            req.transition(BitmapTransitionOptions.withCrossFade(factory))
        }

        override fun setThumbnail() {
            req.thumbnail(GlideApp.with(iv).asBitmap().load(thumbnail))
        }

        override fun onError(requestListener: RequestListener<Bitmap>?) {
            req.error(GlideApp.with(iv).asBitmap().load(reserveUrl).listener(requestListener))
        }
    }
}