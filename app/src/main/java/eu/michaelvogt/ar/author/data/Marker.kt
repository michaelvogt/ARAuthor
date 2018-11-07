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

import androidx.room.*
import com.google.ar.sceneform.math.Vector3

val TITLES_ONLY = arrayOf(1)
val MARKERS_ONLY = arrayOf(0)
val MARKERS_AND_TITLES = arrayOf(0, 1)

@Entity(
        tableName = "markers",
        foreignKeys = [ForeignKey(
                entity = Location::class,
                parentColumns = arrayOf("u_id"),
                childColumns = arrayOf("location_id"),
                onDelete = ForeignKey.CASCADE)],
        indices = [
            Index(value = ["title"]),
            Index(value = ["location_id"])
        ])
class Marker {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0

    @ColumnInfo(name = "location_id")
    var locationId: Long = 0

    @ColumnInfo(name = "thumb_path")
    var thumbPath: String = ""

    @ColumnInfo(name = "image_path")
    var markerImagePath: String = ""

    @ColumnInfo(name = "background_image_path")
    var backgroundImagePath: String = ""

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
    var size: Vector3 = Vector3.one()

    @ColumnInfo(name = "zero_point")
    var zeroPoint: Vector3 = Vector3.zero()

    @ColumnInfo(name = "show_background")
    var isShowBackground: Boolean = false

    @Ignore
    constructor() : this( 0,"")

    constructor(locationId: Long,
                title: String,
                markerImagePath: String = "",
                backgroundImagePath: String = "",
                intro: String? = null,
                location: String = "",
                widthInM: Float = -1f,
                zeroPoint: Vector3 = Vector3.zero(),
                size: Vector3 = Vector3.one(),
                isShowBackground: Boolean = false,
                isTitle: Boolean = false) {
        this.markerImagePath = markerImagePath
        this.title = title
        this.intro = intro
        this.location = location
        this.widthInM = widthInM
        this.zeroPoint = zeroPoint
        this.size = size
        this.backgroundImagePath = backgroundImagePath
        this.isShowBackground = isShowBackground
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

    fun hasImage(): Boolean {
        return !markerImagePath.isEmpty()
    }

    fun isTitle(): Boolean {
        return isTitle
    }

    fun setTitle(title: Boolean) {
        isTitle = title
    }

    override fun toString(): String {
        return "Marker(uId=$uId, locationId=$locationId, thumbPath='$thumbPath', markerImagePath='$markerImagePath', backgroundImagePath=$backgroundImagePath, title='$title', intro=$intro, isTitle=$isTitle, location=$location, widthInM=$widthInM, size=$size, zeroPoint=$zeroPoint, isShowBackground=$isShowBackground)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Marker

        if (uId != other.uId) return false
        if (locationId != other.locationId) return false
        if (thumbPath != other.thumbPath) return false
        if (markerImagePath != other.markerImagePath) return false
        if (backgroundImagePath != other.backgroundImagePath) return false
        if (title != other.title) return false
        if (intro != other.intro) return false
        if (isTitle != other.isTitle) return false
        if (location != other.location) return false
        if (widthInM != other.widthInM) return false
        if (!Vector3.equals(size, other.size)) return false
        if (!Vector3.equals(zeroPoint, other.zeroPoint)) return false
        if (isShowBackground != other.isShowBackground) return false

        return true
    }

    override fun hashCode() : Int {
        var result = uId
        result = 31 * result + locationId
        result = 31 * result + thumbPath.hashCode()
        result = 31 * result + markerImagePath.hashCode()
        result = 31 * result + (backgroundImagePath.hashCode())
        result = 31 * result + title.hashCode()
        result = 31 * result + (intro?.hashCode() ?: 0)
        result = 31 * result + isTitle.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + widthInM.hashCode()
        result = 31 * result + (size.hashCode())
        result = 31 * result + (zeroPoint.hashCode())
        result = 31 * result + isShowBackground.hashCode()
        return result.toInt()
    }

    companion object {
        const val MIN_SIZE = 300
    }


}
