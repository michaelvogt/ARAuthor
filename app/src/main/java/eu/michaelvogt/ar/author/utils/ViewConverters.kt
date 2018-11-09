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

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod

@InverseMethod("floatFromString")
fun stringFromFloat(value: Float): String {
    return when (value) {
        Float.NaN -> ""
        else -> value.toString()
    }
}

fun floatFromString(value: String): Float {
    return value.toFloat()
}


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
        view.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(url, 100, 100))
    }
}