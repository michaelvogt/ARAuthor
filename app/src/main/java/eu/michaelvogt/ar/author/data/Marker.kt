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

package eu.michaelvogt.ar.author.data

import android.arch.persistence.room.*
import com.google.ar.sceneform.math.Vector3

@Entity(
        tableName = "markers",
        foreignKeys = [ForeignKey(
                entity = Location::class,
                parentColumns = arrayOf("u_id"),
                childColumns = arrayOf("location_id"))])
class Marker {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Int = 0

    @ColumnInfo(name = "location_id")
    var locationId: Int = 0

    @ColumnInfo(name = "thumb_path")
    var thumbPath: String = ""

    @ColumnInfo(name = "image_path")
    var markerImagePath: String = ""

    @ColumnInfo(name = "background_image_path")
    var backgroundImagePath: String? = null

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "intro")
    var intro: String? = null

    @ColumnInfo(name = "is_title")
    private var isTitle = false

    @ColumnInfo(name = "location")
    var location: String? = null

    @ColumnInfo(name = "width_in_m")
    var widthInM = -1f

    @ColumnInfo(name = "size")
    var size: Vector3? = Vector3.one()

    @ColumnInfo(name = "zero_point")
    var zeroPoint: Vector3? = Vector3.zero()

    @ColumnInfo(name = "show_background")
    var isShowBackground: Boolean = false

    @Ignore
    constructor() : this( 0,"", "", "", "",
            "", .0f, Vector3.zero(), Vector3.zero(), true,  false)

    constructor(locationId: Int = 0,
                title: String,
                markerImagePath: String = "",
                backgroundImagePath: String? = null,
                intro: String? = null,
                location: String = "",
                widthInM: Float = -1f,
                zeroPoint: Vector3 = Vector3.zero(),
                size: Vector3 = Vector3.one(),
                showBackground: Boolean = false,
                isTitle: Boolean = false) {
        this.markerImagePath = markerImagePath
        this.title = title
        this.intro = intro
        this.location = location
        this.widthInM = widthInM
        this.zeroPoint = zeroPoint
        this.size = size
        this.backgroundImagePath = backgroundImagePath
        this.isShowBackground = showBackground
        this.locationId = locationId
        this.isTitle = isTitle
        this.thumbPath = ""
    }

    @Ignore
    constructor(marker: Marker) {
        this.markerImagePath = marker.markerImagePath
        this.title = marker.title
        this.isTitle = marker.isTitle()
        this.intro = marker.intro
        this.location = marker.location
        this.widthInM = marker.widthInM
        this.zeroPoint = marker.zeroPoint
        this.size = marker.size
        this.backgroundImagePath = marker.backgroundImagePath
        this.isShowBackground = marker.isShowBackground
        this.thumbPath = marker.thumbPath
        this.locationId = marker.locationId
    }

    // TODO: Hack - Replace with Location object
    @Ignore
    constructor(locationId: Int, title: String) {
        this.title = title
        this.isTitle = true
        this.locationId = locationId
    }

    fun hasImage(): Boolean {
        return !markerImagePath.isEmpty()
    }

    fun isTitle(): Boolean {
        return isTitle
    }

    fun setTitle(title: Boolean) {
        isTitle = title
    }

    companion object {
        const val MIN_SIZE = 300
    }
}
