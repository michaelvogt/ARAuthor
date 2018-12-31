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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3

const val GROUP_ALL = -1
const val GROUP_NONE = 0
const val GROUP_START = 1

val GROUPS_ALL = arrayOf(GROUP_ALL, GROUP_START, GROUP_NONE)

@Entity(tableName = "areas")
class Area {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0

    @ColumnInfo(name = "object_type")
    var objectType: Int = TYPE_DEFAULT

    @ColumnInfo(name = "usage_type")
    var usageType: Int = TYPE_DEFAULT

    @ColumnInfo(name = "coord_type")
    var coordType: Int = 1

    @ColumnInfo(name = "zero_point")
    var zeroPoint: Vector3 = Vector3.zero()

    var title: String = ""
    var position: Vector3 = Vector3.zero()
    var resource: Int = 0
    var size: Vector3 = Vector3.one()
    var rotation: Quaternion = Quaternion.identity()
    var scale: Vector3 = Vector3.one()
    var group: Int = GROUP_NONE

    constructor(
            title: String, objectType: Int = 0, usageType: Int = 0, group: Int = GROUP_NONE,
            resource: Int = 0, zeroPoint: Vector3 = Vector3.zero(), size: Vector3 = Vector3.one(),
            coordType: Int = COORDINATE_LOCAL, position: Vector3 = Vector3.zero(),
            rotation: Quaternion = Quaternion.identity(), scale: Vector3 = Vector3.one()) {
        this.title = title
        this.objectType = objectType
        this.usageType = usageType
        this.resource = resource
        this.zeroPoint = zeroPoint
        this.size = size
        this.coordType = coordType
        this.position = position
        this.rotation = rotation
        this.scale = scale
        this.group = group
    }

    @Ignore
    constructor(area: Area) {
        this.title = area.title
        this.objectType = area.objectType
        this.usageType = area.usageType
        this.resource = area.resource
        this.zeroPoint = area.zeroPoint
        this.size = area.size
        this.coordType = area.coordType
        this.position = area.position
        this.rotation = area.rotation
        this.scale = area.scale
        this.group = area.group
    }

    override fun toString(): String {
        return "Area(uId=$uId, title='$title', objectType=$objectType, usageType=$usageType)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (uId != other.uId) return false
        if (title != other.title) return false
        if (objectType != other.objectType) return false
        if (usageType != other.usageType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uId
        result = 31 * result + title.hashCode()
        result = 31 * result + objectType
        result = 31 * result + usageType
        return result.toInt()
    }
}
