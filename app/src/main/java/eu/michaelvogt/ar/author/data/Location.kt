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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import eu.michaelvogt.ar.author.data.tuples.SearchLocation

@Entity(
        tableName = "locations",
        indices = [Index(value = ["name", "module_id"], unique = true)])
class Location(
        @field:ColumnInfo(name = "name")
        var name: String,

        var description: String?,

        @field:ColumnInfo(name = "db_path")
        var dbPath: String?,

        @field:ColumnInfo(name = "thumb_path")
        var thumbPath: String?,

        @field:ColumnInfo(name = "intro_html_path")
        var introHtmlPath: String?,

        @field:ColumnInfo(name = "is_default_location")
        var isDefaultLocation: Boolean = false,

        @field:ColumnInfo(name = "content_size")
        var contentSize: String? = "?KB",

        var isLoaded: Boolean? = false,

        @field:ColumnInfo(name = "module_id")
        var moduleId: String? = "") {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0

    override
    fun toString(): String {
        return "Location(name='$name', " +
                "thumbPath=$thumbPath, " +
                "introHtmlPath=$introHtmlPath, " +
                "uId=$uId, " +
                "contentSize=$contentSize, " +
                "isLoaded=$isLoaded)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (name != other.name) return false
        if (description != other.description) return false
        if (thumbPath != other.thumbPath) return false
        if (introHtmlPath != other.introHtmlPath) return false
        if (uId != other.uId) return false
        if (contentSize != other.contentSize) return false
        if (isLoaded != other.isLoaded) return false

        return true
    }

    override
    fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (thumbPath?.hashCode() ?: 0)
        result = 31 * result + (introHtmlPath?.hashCode() ?: 0)
        result = 31 * result + uId.toInt()
        result = 31 * result + (contentSize?.hashCode() ?: 0)
        result = 31 * result + (isLoaded?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun getDefaultLocation(): Location {
            return Location(
                    "Current location",
                    "Your current location",
                    "",
                    "/module_asset/location/images/mylocationthumb.webp",
                    "/module_asset/location/mylocationintro.html",
                    true,
                    "",
                    true)
        }

        fun getNewLocation(): Location {
            return Location(
                    "New location",
                    "Default location for testing",
                    "",
                    "",
                    "",
                    isLoaded = true)
        }

        fun getPlaceholderLocation(searchLocation: SearchLocation): Location {
            return Location(
                    searchLocation.name,
                    searchLocation.description,
                    "",
                    searchLocation.thumb_path,
                    searchLocation.intro_html_path,
                    false,
                    searchLocation.content_size,
                    false,
                    searchLocation.module_id)
        }
    }
}
