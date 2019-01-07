/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package eu.michaelvogt.ar.author.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import eu.michaelvogt.ar.author.R
import java.io.IOException
import java.io.InputStream

object ImageUtils {
    const val assetPathPrefix = "/android_asset/"

    fun decodeSampledBitmapFromImagePath(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        var img = BitmapFactory.decodeFile(path, options)

        img = rotateImageIfRequired(img, path)
        return img
    }

    fun bitmapFromVisualDetail(context: Context, imagePath: String?): Bitmap {
        val bitmap: Bitmap
        var path = imagePath ?: context.getString(R.string.default_background)

        if (path.startsWith(ImageUtils.assetPathPrefix)) {
            var assetStream: InputStream? = null
            try {
                val substring = path.removePrefix(ImageUtils.assetPathPrefix)
                assetStream = context.resources.assets.open(substring)
                bitmap = BitmapFactory.decodeStream(assetStream)
            } catch (e: IOException) {
                e.printStackTrace()
                throw IllegalArgumentException("Background image couldn't be opened", e)
            } finally {
                if (assetStream != null) {
                    try {
                        assetStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            path = FileUtils.getFullPuplicFolderPath(path)
            bitmap = BitmapFactory.decodeFile(path)
        }

        return bitmap
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight && width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun rotateImageIfRequired(img: Bitmap, selectedImage: String): Bitmap {
        val ei = ExifInterface(selectedImage)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270f)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)

        val rotatedImage = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()

        return rotatedImage
    }
}
