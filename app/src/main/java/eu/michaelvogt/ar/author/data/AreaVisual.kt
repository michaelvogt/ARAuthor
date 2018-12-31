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

package eu.michaelvogt.ar.author.data

import android.util.SparseArray
import android.widget.TextView
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.Renderable
import eu.michaelvogt.ar.author.R

const val TYPE_DEFAULT = 0
const val TYPE_3DOBJECTONIMAGE = 1
const val TYPE_3DOBJECTONPLANE = 2
const val TYPE_SLIDESONIMAGE = 3
const val TYPE_INTERACTIVEOVERLAY = 4
const val TYPE_INTERACTIVEPANEL = 5
const val TYPE_TEXTONIMAGE = 6
const val TYPE_IMAGEONIMAGE = 8
const val TYPE_ROTATIONBUTTON = 9
const val TYPE_BACKGROUNDONIMAGE = 10
const val TYPE_COMPARATORONIMAGE = 11

const val KIND_CONTENT = 0
const val KIND_UI = 1
const val KIND_BACKGROUND = 2

const val COORDINATE_LOCAL = 1
const val COORDINATE_GLOBAL = 2

const val ICON_3DOBJECT = R.drawable.ic_account_balance_black_24dp
const val ICON_FLATOVERLAY = R.drawable.ic_collections_black_24dp
const val ICON_INTERACTIVEOVERLAY = R.drawable.ic_gamepad_black_24dp
const val ICON_INTERACTIVEPANEL = R.drawable.ic_my_location_black_24dp

const val BACKGROUNDAREATITLE = "background"
const val DEFAULTAREATITLE = "Default"

// TODO: Get available languages from the database
const val LANGUAGE_EN = "_en_"
const val LANGUAGE_JP = "_jp_"
const val LANGUAGE_DE = "_de_"

class AreaVisual {
    private val MATERIAL_FADELEFTWIDTH = "fadeLeftWidth"
    private val MATERIAL_FADERIGHTWIDTH = "fadeRightWidth"

    constructor(area: Area, details: List<VisualDetail>, events: List<EventDetail>) {
        this.area = area
        this.details = sparsifyDetails(details)
        this.events = sparsifyEvents(events)
    }

    constructor(objectType: Int, usageType: Int, title: String, resource: Int = 0,
                zeroPoint: Vector3 = Vector3.zero(), size: Vector3 = Vector3.one(),
                coordType: Int = COORDINATE_LOCAL, position: Vector3 = Vector3.zero(),
                rotation: Quaternion = Quaternion.identity(), scale: Vector3 = Vector3.one(), group: Int = GROUP_NONE) {
        area = Area(title, objectType, usageType, group, resource, zeroPoint, size, coordType, position, rotation, scale)
        details = SparseArray()
        events = SparseArray()
    }

    constructor(areaVisual: AreaVisual) {
        this.area = areaVisual.area
        this.details = areaVisual.details
        this.events = areaVisual.events
    }

    //  public AreaVisual setImagePath(@NonNull String path) {
    //    if (!path.endsWith("jpg") && !path.endsWith(".png"))
    //      throw new AssertionError("Only .png and .jpg files are supported");
    //
    //    details.add(new VisualDetail(area.getUid(), VisualDetail.TYPE_DETAIL_ALL, KEY_IMAGEPATH, path));
    //    return this;
    //  }
    //
    //  public AreaVisual setSecondaryImagePath(@NonNull String path) {
    //    if (!path.endsWith("jpg") && !path.endsWith(".png"))
    //      throw new AssertionError("Only .png and .jpg files are supported");
    //
    //    details.add(new VisualDetail(area.getUid(), VisualDetail.TYPE_DETAIL_ALL, KEY_SECONDARYIMAGEPATH, path));
    //    return this;
    //  }
    var area: Area
        private set

    val title: String
        get() = area.title

    val objectType: Int
        get() = area.objectType

    val usageType: Int
        get() = area.usageType

    val resource: Int
        get() = area.resource

    val coordType: Int
        get() = area.coordType

    val position: Vector3
        get() = area.position

    val zeroPoint: Vector3
        get() = area.zeroPoint

    val rotation: Quaternion
        get() = area.rotation

    val size: Vector3
        get() = area.size

    val scale: Vector3
        get() = area.scale

    var details: SparseArray<VisualDetail>
        private set

    var events: SparseArray<EventDetail>
        private set

    fun hasDetail(type: Int): Boolean = details.indexOfKey(type) >= 0

    fun getDetail(type: Int): VisualDetail? {
        return details.get(type)
    }

    fun hasEvent(): Boolean = events.size() > 0

    fun getDetailValue(type: Int, orDefault: Any): Any {
        return if (hasDetail(type)) getDetail(type)!!.anyValue else orDefault
    }

    fun getDetailValue(type: Int): Any? = getDetail(type)?.anyValue

    private val value = { key: Int -> getDetailValue(key, 0.0f) as Float }
    fun apply(material: Material) {
        material.setFloat(MATERIAL_FADELEFTWIDTH, 0f)
        material.setFloat(MATERIAL_FADERIGHTWIDTH, value.invoke(KEY_FADE_RIGHT_WIDTH))
    }

    fun apply(textView: TextView) {
        textView.textSize = getDetailValue(KEY_TEXTSIZE) as Float
        textView.setBackgroundColor(getDetailValue(KEY_BACKGROUNDCOLOR) as Int)
    }

    fun apply(renderable: Renderable) {
        renderable.isShadowCaster = (getDetailValue(KEY_ISCASTINGSHADOW) ?: false) as Boolean
    }

    private fun sparsifyDetails(list: List<VisualDetail>): SparseArray<VisualDetail> {
        val array = SparseArray<VisualDetail>(list.size)
        list.forEach { visualDetail -> array.put(visualDetail.key, visualDetail) }
        return array
    }

    private fun sparsifyEvents(list: List<EventDetail>): SparseArray<EventDetail> {
        val array = SparseArray<EventDetail>(list.size)
        list.forEach { eventDetail -> array.put(eventDetail.type, eventDetail) }
        return array
    }

    companion object {
        fun getDefaultArea(backgroundHeight: Float, backgroundWidth: Float): AreaVisual {
            return AreaVisual(TYPE_DEFAULT, KIND_CONTENT, DEFAULTAREATITLE, R.raw.default_model,
                    Vector3.zero(), Vector3.one(), COORDINATE_LOCAL, Vector3(-backgroundWidth / 2, 0f, -backgroundHeight / 2),
                    Quaternion(Vector3(0f, 1f, 0f), 180f), Vector3.one())
        }

        fun getBackgroundArea(marker: Marker, path: String): AreaVisual {
            val areaVisual = AreaVisual(TYPE_BACKGROUNDONIMAGE, KIND_BACKGROUND, BACKGROUNDAREATITLE,
                    0, marker.zeroPoint, marker.size, COORDINATE_LOCAL, Vector3(0f, 0.1f, 0f),
                    Quaternion(Vector3.zero(), 0f), Vector3.one())
            areaVisual.details.put(KEY_IMAGEPATH, VisualDetail(0, KEY_IMAGEPATH, 0, path as Any))
            return areaVisual
        }
    }
}
