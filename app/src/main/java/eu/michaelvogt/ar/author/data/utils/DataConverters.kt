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

import androidx.room.TypeConverter
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import java.util.*

const val VALUE_DIVIDER = "!!"

const val TAG_BOOL = "<bool>"
const val TAG_INT = "<int>"
const val TAG_LONG = "<long>"
const val TAG_FLOAT = "<float>"
const val TAG_LIST = "<list>"
const val TAG_VECTOR3 = "<vector3>"
const val TAG_QUATERNION = "<quaternion>"
const val TAG_FLOATLIST = "<floatlist>"

class Converters {
    // TODO: Use kotlin.serializable when usable

    @TypeConverter
    fun vector3FromString(value: String?): Vector3? {
        if (value == null) return null
        if (!value.startsWith(TAG_VECTOR3))
            throw IllegalArgumentException("Delivered parameter isn't a vector3: $value")

        val content = value.split(TAG_VECTOR3.toRegex())[1]
        val split = content.split(VALUE_DIVIDER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Vector3(
                java.lang.Float.parseFloat(split[0]), java.lang.Float.parseFloat(split[1]), java.lang.Float.parseFloat(split[2]))
    }

    @TypeConverter
    fun vector3ToString(vector3: Vector3?): String {
        return if (vector3 == null)
            ""
        else
            TAG_VECTOR3 + vector3.x + VALUE_DIVIDER + vector3.y + VALUE_DIVIDER + vector3.z
    }


    @TypeConverter
    fun quaternionFromString(value: String?): Quaternion? {
        if (value == null) return null

        val content = value.split(TAG_QUATERNION)[1]
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
            TAG_QUATERNION + quaternion.x.toString() + VALUE_DIVIDER + quaternion.y +
                    VALUE_DIVIDER + quaternion.z + VALUE_DIVIDER + quaternion.w
    }

    @TypeConverter
    fun stringListFromString(value: String): List<String> {
        return value.split(VALUE_DIVIDER)
    }

    @TypeConverter
    fun stringListToString(list: List<String>?): String {
        return list?.joinToString(separator = VALUE_DIVIDER) ?: "[]"
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    fun listToString(value: List<*>): String {
        if (value.isEmpty()) return value.toString()

        return when (value[0]) {
            is Float -> value.joinToString(prefix = TAG_FLOATLIST)
            else -> value.toString()
        }
    }

    fun stringify(value: Any): String {
        return when (value) {
            is Boolean -> if (value) "${TAG_BOOL}1" else "${TAG_BOOL}0"
            is Int -> TAG_INT + value
            is Long -> TAG_LONG + value
            is Float -> TAG_FLOAT + value
            is List<*> -> listToString(value)
            is Vector3 -> vector3ToString(value)
            is Quaternion -> quaternionToString(value)
            else -> value.toString()
        }
    }

    fun objectify(string: String): Any {
        return when {
            string.startsWith(TAG_BOOL) -> string == "${TAG_BOOL}1"
            string.startsWith(TAG_INT) -> string.removePrefix(TAG_INT).toInt()
            string.startsWith(TAG_LONG) -> string.removePrefix(TAG_LONG).toLong()
            string.startsWith(TAG_FLOAT) -> string.removePrefix(TAG_FLOAT).toFloat()
            string.startsWith(TAG_VECTOR3) -> vector3FromString(string) as Any
            string.startsWith(TAG_QUATERNION) -> quaternionFromString(string) as Any
            string.startsWith(TAG_FLOATLIST) -> string.removePrefix(TAG_FLOATLIST).split(",").map { it.toFloat() }
            string.startsWith("[") -> {
                val value = string.removePrefix("[").removeSuffix("]")
                if (value.isEmpty()) emptyList() else value.split(",").toList()
            }
            else -> string
        }
    }
}
