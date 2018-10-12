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

package eu.michaelvogt.ar.author.data.utils

import android.arch.persistence.room.TypeConverter
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import java.lang.IllegalArgumentException
import java.util.*

class Converters {
    private val DIVIDER_VECTOR3 = "!!"
    private val DIVIDER_QUATERNION = "!!"

    // TODO: Use kotlin.serializable when its working

    @TypeConverter
    fun vector3FromString(value: String?): Vector3? {
        if (value == null) return null
        if (!value.startsWith("<vector3>"))
            throw IllegalArgumentException("Delivered parameter isn't a vector3: " + value)

        val content = value.split("<vector3>".toRegex()).get(1)
        val split = content.split(DIVIDER_VECTOR3.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Vector3(
                java.lang.Float.parseFloat(split[0]), java.lang.Float.parseFloat(split[1]), java.lang.Float.parseFloat(split[2]))
    }

    @TypeConverter
    fun vector3ToString(vector3: Vector3?): String {
        return if (vector3 == null)
            ""
        else
            "<vector3>" + vector3.x + DIVIDER_VECTOR3 + vector3.y + DIVIDER_VECTOR3 + vector3.z + "<vector3>"
    }


    @TypeConverter
    fun quaternionFromString(value: String?): Quaternion? {
        if (value == null) return null

        val content = value.split("<quaternion>").get(1)
        val split = content.split(DIVIDER_QUATERNION.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val result = Quaternion()
        result.x = java.lang.Float.parseFloat(split[0])
        result.y = java.lang.Float.parseFloat(split[1])
        result.z = java.lang.Float.parseFloat(split[2])
        result.w = java.lang.Float.parseFloat(split[3])

        return result
    }

    @TypeConverter
    fun quaternionToString(quaternion: Quaternion?): String {
        return if (quaternion == null)
            ""
        else
            "<quaternion>" + quaternion.x.toString() + DIVIDER_QUATERNION + quaternion.y +
                    DIVIDER_QUATERNION + quaternion.z + DIVIDER_QUATERNION + quaternion.w + "<quaternion>"
    }


    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    fun stringify(value: Any): String {
        return when (value) {
            is Vector3 -> vector3ToString(value)
            is Quaternion -> quaternionToString(value)
            else -> value.toString()
        }
    }

    fun objectify(`object`: String): Any {
        return ""
    }
}
