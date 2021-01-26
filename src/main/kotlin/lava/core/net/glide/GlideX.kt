package lava.core.net.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import lava.core.obj.AppConfig
import java.io.InputStream

/**
 * Created by svc on 2021/1/26
 */

@GlideModule
class GlideX : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSizeBytes = AppConfig.diskCacheSize
        // 路径规则: sdcard/Android/data/包名/cache/diskCacheName
        builder.setDiskCache(
            ExternalPreferredCacheDiskCacheFactory(
                context,
                AppConfig.imageDiskName,
                diskCacheSizeBytes
            )
        )

        val calc = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(3f)
            .setMemoryCacheScreens(2f)
            .build()
        builder.setMemoryCache(LruResourceCache(calc.memoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(calc.bitmapPoolSize.toLong()))

        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory()
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}