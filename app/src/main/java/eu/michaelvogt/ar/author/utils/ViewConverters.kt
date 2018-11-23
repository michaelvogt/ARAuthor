/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018  Michael Vogt

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

@file:JvmName("ViewConverters")

package eu.michaelvogt.ar.author.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import com.google.ar.sceneform.math.Vector3
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.fragments.AreaEditCard

@InverseMethod("floatFromString")
fun floatToString(value: Float): String {
    return when (value) {
        Float.NaN -> ""
        else -> value.toString()
    }
}

fun floatFromString(value: String): Float = value.toFloat()

@InverseMethod("booleanFromString")
fun stringFromBoolean(value: Boolean): String {
    return if (value) "1" else "0"
}

fun booleanFromString(value: String): Boolean {
    return value.equals("1")
}

@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
fun setImage(view: ImageView, url: String, placeholder: Drawable) {
    if (url.isEmpty()) {
        view.setImageDrawable(placeholder)
    } else {
        val bitmap: Bitmap
        try {
            bitmap = ImageUtils.decodeSampledBitmapFromImagePath(url, 100, 100)
            view.setImageBitmap(bitmap)
        } catch (e: IllegalStateException) {
            Log.i("ViewConverters", "Image not found", e)
            view.setImageResource(R.drawable.touristar_logo)
        }
    }
}

@BindingAdapter("values")
fun setVector3(view: AreaEditCard, vector3: Vector3?) {
    val string = when (vector3) {
        null -> ""
        else -> vector3.toString()
    }
    view.setValues(string)
}