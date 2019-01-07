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

import androidx.room.*
import com.google.ar.sceneform.math.Vector3

@Entity(
        tableName = "markers",
        foreignKeys = [
            ForeignKey(
                    entity = Location::class,
                    parentColumns = arrayOf("u_id"),
                    childColumns = arrayOf("location_id"),
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = TitleGroup::class,
                    parentColumns = arrayOf("u_id"),
                    childColumns = arrayOf("group_id"),
                    onDelete = ForeignKey.CASCADE)],
        indices = [
            Index(value = ["title"]),
            Index(value = ["location_id"]),
            Index(value = ["group_id"])
        ])
class Marker {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0

    @ColumnInfo(name = "location_id")
    var locationId: Long

    @ColumnInfo(name = "group_id")
    var groupId: Long?

    @ColumnInfo(name = "thumb_path")
    var thumbPath: String

    @ColumnInfo(name = "image_path")
    var markerImagePath: String

    @ColumnInfo(name = "background_image_path")
    var backgroundImagePath: String

    @ColumnInfo(name = "title")
    var title: String

    @ColumnInfo(name = "intro")
    var intro: String?

    @ColumnInfo(name = "place")
    var place: String?

    @ColumnInfo(name = "width_in_m")
    var widthInM: Float

    @ColumnInfo(name = "size")
    var size: Vector3

    @ColumnInfo(name = "zero_point")
    var zeroPoint: Vector3

    @ColumnInfo(name = "show_background")
    var isShowBackground: Boolean

    @Ignore
    constructor() : this(0, null, "New Marker")

    constructor(locationId: Long,
                groupId: Long?,
                title: String,
                markerImagePath: String = "",
                backgroundImagePath: String = "/android_asset/location/images/mylocationback.webp",
                intro: String? = null,
                place: String = "",
                widthInM: Float = 0.2f,     // ARCore crashes when width isn't set
                zeroPoint: Vector3 = Vector3.zero(),
                size: Vector3 = Vector3.one(),
                isShowBackground: Boolean = false) {
        this.markerImagePath = markerImagePath
        this.title = title
        this.intro = intro
        this.place = place
        this.widthInM = widthInM
        this.zeroPoint = zeroPoint
        this.size = size
        this.backgroundImagePath = backgroundImagePath
        this.isShowBackground = isShowBackground
        this.locationId = locationId
        this.groupId = groupId
        this.thumbPath = ""
    }

    @Ignore
    constructor(marker: Marker) {
        this.markerImagePath = marker.markerImagePath
        this.title = marker.title
        this.intro = marker.intro
        this.place = marker.place
        this.widthInM = marker.widthInM
        this.zeroPoint = marker.zeroPoint
        this.size = marker.size
        this.backgroundImagePath = marker.backgroundImagePath
        this.isShowBackground = marker.isShowBackground
        this.thumbPath = marker.thumbPath
        this.locationId = marker.locationId
        this.groupId = marker.groupId
    }

    fun hasImage(): Boolean {
        return !markerImagePath.isEmpty()
    }

    // Needed for 2-way databinding
    fun setIsShowBackground(value: Boolean) {
        isShowBackground = value
    }

    override
    fun toString(): String {
        return """
            Marker(
            uId=$uId,
            locationId=$locationId,
            thumbPath='$thumbPath',
            markerImagePath='$markerImagePath',
            backgroundImagePath=$backgroundImagePath,
            title='$title',
            intro=$intro,
            place=$place,
            widthInM=$widthInM,
            size=$size,
            zeroPoint=$zeroPoint,
            isShowBackground=$isShowBackground)""".trimMargin()
    }

    override
    fun equals(other: Any?): Boolean {
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
        if (place != other.place) return false
        if (widthInM != other.widthInM) return false
        if (!Vector3.equals(size, other.size)) return false
        if (!Vector3.equals(zeroPoint, other.zeroPoint)) return false
        if (isShowBackground != other.isShowBackground) return false

        return true
    }

    override
    fun hashCode(): Int {
        var result = uId
        result = 31 * result + locationId
        result = 31 * result + thumbPath.hashCode()
        result = 31 * result + markerImagePath.hashCode()
        result = 31 * result + (backgroundImagePath.hashCode())
        result = 31 * result + title.hashCode()
        result = 31 * result + (intro?.hashCode() ?: 0)
        result = 31 * result + (place?.hashCode() ?: 0)
        result = 31 * result + widthInM.hashCode()
        result = 31 * result + (size.hashCode())
        result = 31 * result + (zeroPoint.hashCode())
        result = 31 * result + isShowBackground.hashCode()
        return result.toInt()
    }

    companion object {
        const val MIN_SIZE = 300

        /**
         * Simple in-memory representation of a [Marker]. Not meant to be saved to DB
         */
        fun getDefaultMarker(): Marker {
            return Marker(0, null, "My Marker")
        }
    }
}
