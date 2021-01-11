package lava.core.safe

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * Created by svc on 2021/01/09
 */
class BitmapCreator {
    companion object {
        fun create(src: Bitmap): Bitmap? {
            var ss: Boolean? = false

            return kotlin.runCatching {
                Bitmap.createBitmap(src, 0, 0, src.width, src.height)
            }.getOrNull()
        }

        fun create(source: Bitmap, x: Int, y: Int, width: Int, height: Int, matrix: Matrix, filter: Boolean): Bitmap? {
            return kotlin.runCatching {
                Bitmap.createBitmap(source, x, y, width, height, matrix, filter)
            }.getOrNull()
        }

        fun create(source: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap? {
            return kotlin.runCatching {
                Bitmap.createBitmap(source, x, y, width, height)
            }.getOrNull()
        }
    }
}