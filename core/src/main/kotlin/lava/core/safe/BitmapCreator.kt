package lava.core.safe

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import lava.core.util.AppUtil

/**
 * Created by svc on 2021/01/09
 */
class BitmapCreator {
    companion object {
        fun getSize(path: String): BitmapFactory.Options {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)
            return options
        }

        fun fixSampleSizeByMem(options: BitmapFactory.Options): BitmapFactory.Options {
            val outWidth = options.outWidth
            val outHeight = options.outHeight
            val area = outWidth * outHeight
            val avail = AppUtil.getAvailMem() / 4

            options.inSampleSize = Integer.highestOneBit((area / avail.toFloat()).toInt()).shl(1)
            return options
        }

        fun create(src: Bitmap): Bitmap? {
            return kotlin.runCatching {
                if (available(src.width, src.height)) {
                    Bitmap.createBitmap(src, 0, 0, src.width, src.height)
                } else null
            }.getOrNull()
        }

        fun create(source: Bitmap, x: Int, y: Int, width: Int, height: Int, matrix: Matrix, filter: Boolean): Bitmap? {
            return kotlin.runCatching {
                if (available(width, height)) {
                    Bitmap.createBitmap(source, x, y, width, height, matrix, filter)
                } else null
            }.getOrNull()
        }

        fun create(source: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap? {
            return kotlin.runCatching {
                if (available(width, height)) {
                    Bitmap.createBitmap(source, x, y, width, height)
                } else null
            }.getOrNull()
        }

        /**
         * ARGB_8888 4 byte
         * ARGB_4444 2 byte
         * RGB_565   2 byte
         * ALPHA_*   1 byte
         */
        private fun available(width: Int, height: Int, byteRate: Int = 4): Boolean {
            val needMem = width * height * byteRate
            val available = AppUtil.getAvailMem()
            return needMem < available
        }
    }
}