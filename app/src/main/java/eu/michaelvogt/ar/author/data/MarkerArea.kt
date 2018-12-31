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
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
        tableName = "marker_areas",
        primaryKeys = ["marker_id", "area_id"],
        foreignKeys = [
            ForeignKey(
                    entity = Marker::class,
                    parentColumns = arrayOf("u_id"),
                    childColumns = arrayOf("marker_id"),
                    onDelete = ForeignKey.CASCADE),       // TODO: Need to check
            ForeignKey(
                    entity = Area::class,
                    parentColumns = arrayOf("u_id"),
                    childColumns = arrayOf("area_id"))],
        indices = [
            Index(value = ["area_id"]
            )
        ])
class MarkerArea(
        @field:ColumnInfo(name = "marker_id")
        var markerId: Long,

        @field:ColumnInfo(name = "area_id")
        var areaId: Long)
