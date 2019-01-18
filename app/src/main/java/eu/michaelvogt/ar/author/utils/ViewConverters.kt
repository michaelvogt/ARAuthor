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

@file:JvmName("ViewConverters")

package eu.michaelvogt.ar.author.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.SparseArray
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseMethod
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*
import eu.michaelvogt.ar.author.data.utils.Converters
import eu.michaelvogt.ar.author.fragments.AreaEditCard
import java.io.FileNotFoundException

@InverseMethod("floatFromString")
fun floatToString(value: Float): String {
    return when (value) {
        Float.NaN -> ""
        else -> value.toString()
    }
}

/**
 * Convert [String] value to [Float]
 *
 * It is assumed that the value parameter is provided from user entry into a number field. The
 * [NumberFormatException] should then be ok to ignore, returning '0' as result.
 *
 * @param   String  value entered by user into number field
 * @return  Float   the converted value when conversion succeeds, 0 (zero) otherwise
 */
fun floatFromString(value: String): Float {
    var float = 0f
    try {
        float = value.toFloat()
    } catch (e: NumberFormatException) {
        // Ignored
    }

    return float
}

@InverseMethod("booleanFromString")
fun stringFromBoolean(value: Boolean): String {
    return if (value) "1" else "0"
}

fun booleanFromString(value: String): Boolean {
    return value == "1"
}

@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
fun setImage(view: ImageView, url: String, placeholder: Drawable) {
    if (url.isEmpty()) {
        view.setImageDrawable(placeholder)
    } else if (url.startsWith(ImageUtils.assetPathPrefix)) {
        try {
            val asset = view.resources.assets.open(url.removePrefix(ImageUtils.assetPathPrefix))
            view.setImageDrawable(Drawable.createFromStream(asset, null))
        } catch (e: FileNotFoundException) {
            Log.i("ViewConverters", "Image not found", e)
            view.setImageResource(R.drawable.touristar_logo)
        }
    } else {
        val bitmap: Bitmap
        try {
            val fullPath = FileUtils.getFullPuplicFolderPath(url)
            bitmap = ImageUtils.decodeSampledBitmapFromImagePath(fullPath, 100, 100)
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
        null -> {
            view.setXValue("")
            view.setYValue("")
            view.setZValue("")
            "No Value"
        }
        else -> {
            view.setXValue(vector3.x.toString())
            view.setYValue(vector3.y.toString())
            view.setZValue(vector3.z.toString())
            "X: ${vector3.x},  Y: ${vector3.y},  Z: ${vector3.z}"
        }
    }
    view.setValue(string)
}

@InverseBindingAdapter(attribute = "values")
fun getVector3(view: AreaEditCard): Vector3 {
    return Converters().vector3FromString(
            "<vector3>${view.getXValue()}!!${view.getYValue()}!!${view.getZValue()}")
            ?: Vector3.zero()
}

@BindingAdapter("values")
fun setQuaternion(view: AreaEditCard, quaternion: Quaternion?) {
    val string = when (quaternion) {
        null -> {
            view.setXValue("")
            view.setYValue("")
            view.setZValue("")
            view.setWValue("")
            "No Value"
        }
        else -> {
            view.setXValue(quaternion.x.toString())
            view.setYValue(quaternion.y.toString())
            view.setZValue(quaternion.z.toString())
            view.setWValue(quaternion.w.toString())
            "X: ${quaternion.x},  Y: ${quaternion.y},  Z: ${quaternion.z},  W: ${quaternion.w}"
        }
    }
    view.setValue(string)
}

@InverseBindingAdapter(attribute = "values")
fun getQuaternion(view: AreaEditCard): Quaternion {
    return Converters().quaternionFromString(
            "<quaternion>${view.getXValue()}!!${view.getYValue()}!!${view.getZValue()}!!${view.getWValue()}")
            ?: Quaternion.identity()
}

@BindingAdapter("values")
fun setString(view: AreaEditCard, string: String?) {
    view.setXValue(string ?: "")
    view.setValue(string ?: "")
}

@InverseBindingAdapter(attribute = "values")
fun getString(view: AreaEditCard): Any {
    return view.getValue()
}

@BindingAdapter("values")
fun setInteger(view: AreaEditCard, int: Int) {
    val array: SparseArray<String> = when (view.id) {
        R.id.areaEditCoordType -> SparseArray<String>().apply {
            put(COORDINATE_GLOBAL, "Global Coordinates")
            put(COORDINATE_LOCAL, "Local Coordinates")
        }
        R.id.areaEditUsageType -> SparseArray<String>().apply {
            put(KIND_UI, "UI Element")
            put(KIND_CONTENT, "Content Element")
            put(KIND_BACKGROUND, "Background")
        }
        R.id.areaEditObjectType -> SparseArray<String>().apply {
            // TODO: GEt the object types from available areas
            put(TYPE_DEFAULT, "Default")
            put(TYPE_3DOBJECTONIMAGE, "3D Objects on Image")
            put(TYPE_3DOBJECTONPLANE, "3D Object on Plane")
            put(TYPE_SLIDESONIMAGE, "Slide on Image")
            put(TYPE_INTERACTIVEOVERLAY, "Interactive Overlay")
            put(TYPE_INTERACTIVEPANEL, "Interactive Panel")
            put(TYPE_TEXTONIMAGE, "Text on Image")
            put(TYPE_IMAGEONIMAGE, "Image on Image")
            put(TYPE_ROTATIONBUTTON, "Rotation on Button")
            put(TYPE_BACKGROUNDONIMAGE, "Background Image")
            put(TYPE_COMPARATORONIMAGE, "Comaparator Image")
        }
        R.id.areaEditGroup -> SparseArray<String>().apply {
            put(GROUP_ALL, "All Groups")
            put(GROUP_START, "Start Group")
            put(GROUP_NONE, "No Group")
        }
        else -> SparseArray()
    }

    view.setValue(array[int] ?: "Unknown")
    view.setSValue(int, array)
}

@InverseBindingAdapter(attribute = "values")
fun getInteger(view: AreaEditCard): Int {
    return view.getSValueId()
}

@BindingAdapter(value = ["onValuesChanged", "valuesAttrChanged"], requireAll = false)
fun setListeners(view: AreaEditCard, listener: AreaEditCard.OnValuesChangeListener?, valuesChange: InverseBindingListener?) {
    if (valuesChange == null) {
        view.onValuesChangeListener = listener
    } else {
        view.onValuesChangeListener = object : AreaEditCard.OnValuesChangeListener {
            override fun onValuesChanged(areaEditCard: AreaEditCard) {
                listener?.onValuesChanged(areaEditCard)
                valuesChange.onChange()
            }
        }
    }
}