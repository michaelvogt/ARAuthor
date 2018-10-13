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
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3

@Entity(tableName = "areas")
class Area {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "object_type")
    var objectType: Int = TYPE_DEFAULT

    @ColumnInfo(name = "usage_type")
    var usageType: Int = TYPE_DEFAULT

    @ColumnInfo(name = "coord_type")
    var coordType: Int = 0

    @ColumnInfo(name = "position")
    var position: Vector3 = Vector3.zero()

    @ColumnInfo(name = "zero_point")
    var zeroPoint: Vector3 = Vector3.zero()

    @ColumnInfo(name = "resource")
    var resource: Int = 0

    @ColumnInfo(name = "size")
    var size: Vector3 = Vector3.one()

    @ColumnInfo(name = "rotation")
    var rotation: Quaternion = Quaternion.identity()

    @ColumnInfo(name = "scale")
    var scale: Vector3 = Vector3.one()

    constructor(
            title: String, objectType: Int = 0, usageType: Int = 0, resource: Int = 0,
            zeroPoint: Vector3 = Vector3.zero(), size: Vector3 = Vector3.one(),
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

    }

    @Ignore
    constructor(area: Area) {
        this.title = area.title
        this.objectType = area.objectType
        this.usageType = area.usageType
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
