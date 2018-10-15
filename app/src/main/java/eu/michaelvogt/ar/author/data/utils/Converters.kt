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
import android.graphics.Color
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import java.lang.IllegalArgumentException
import java.util.*

const val VALUE_DIVIDER = "!!"

const val BOOL_TAG = "<bool>"
const val INT_TAG = "<int>"
const val FLOAT_TAG = "<float>"
const val LIST_TAG = "<list>"
const val VECTOR3_TAG = "<vector3>"
const val QUATERNION_TAG = "<quaternion>"

class Converters {
    // TODO: Use kotlin.serializable

    @TypeConverter
    fun vector3FromString(value: String?): Vector3? {
        if (value == null) return null
        if (!value.startsWith(VECTOR3_TAG))
            throw IllegalArgumentException("Delivered parameter isn't a vector3: " + value)

        val content = value.split(VECTOR3_TAG.toRegex()).get(1)
        val split = content.split(VALUE_DIVIDER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Vector3(
                java.lang.Float.parseFloat(split[0]), java.lang.Float.parseFloat(split[1]), java.lang.Float.parseFloat(split[2]))
    }

    @TypeConverter
    fun vector3ToString(vector3: Vector3?): String {
        return if (vector3 == null)
            ""
        else
            VECTOR3_TAG + vector3.x + VALUE_DIVIDER + vector3.y + VALUE_DIVIDER + vector3.z
    }


    @TypeConverter
    fun quaternionFromString(value: String?): Quaternion? {
        if (value == null) return null

        val content = value.split(QUATERNION_TAG).get(1)
        val split = content.split(VALUE_DIVIDER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

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
            QUATERNION_TAG + quaternion.x.toString() + VALUE_DIVIDER + quaternion.y +
                    VALUE_DIVIDER + quaternion.z + VALUE_DIVIDER + quaternion.w
    }

    @TypeConverter
    fun stringListFromString(value: String): List<String> {
        return value.split(VALUE_DIVIDER)
    }

    @TypeConverter
    fun stringListToString(list: List<String>): String {
        return list.joinToString(separator = VALUE_DIVIDER)
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
            is Boolean -> if (value) "${BOOL_TAG}1" else "${BOOL_TAG}0"
            is Int -> INT_TAG + value
            is Float -> FLOAT_TAG + value
            is List<*> -> value.toString()
            is Vector3 -> vector3ToString(value)
            is Quaternion -> quaternionToString(value)
            else -> value.toString()
        }
    }

    fun objectify(string: String): Any {
        return when {
            string.startsWith(BOOL_TAG) -> string.equals("${BOOL_TAG}1")
            string.startsWith(INT_TAG) -> string.removePrefix(INT_TAG).toInt()
            string.startsWith(FLOAT_TAG) -> string.removePrefix(FLOAT_TAG).toFloat()
            string.startsWith("[") -> string.removePrefix("[").removeSuffix("]").split(",").toList()
            string.startsWith(VECTOR3_TAG) -> vector3FromString(string) as Any
            string.startsWith(QUATERNION_TAG) -> quaternionFromString(string) as Any
            else -> string
        }
    }
}
