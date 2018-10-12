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

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = "locations",
        indices = [Index(value = ["name"], unique = true)])
class Location(
        @field:ColumnInfo(name = "name")
        var name: String,

        @field:ColumnInfo(name = "thumb_path")
        var thumbPath: String?,

        @field:ColumnInfo(name = "intro_html_path")
        var introHtmlPath: String?) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0

    override fun toString(): String {
        return "Location(name='$name', thumbPath=$thumbPath, introHtmlPath=$introHtmlPath, uId=$uId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (name != other.name) return false
        if (thumbPath != other.thumbPath) return false
        if (introHtmlPath != other.introHtmlPath) return false
        if (uId != other.uId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (thumbPath?.hashCode() ?: 0)
        result = 31 * result + (introHtmlPath?.hashCode() ?: 0)
        result = 31 * result + uId.toInt()
        return result
    }


}
