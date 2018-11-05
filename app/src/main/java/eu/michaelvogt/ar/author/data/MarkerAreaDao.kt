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

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MarkerAreaDao : BaseDao<MarkerArea> {
    @Query("SELECT * from marker_areas")
    fun getAll(): List<MarkerArea>

    @Query("SELECT COUNT(*) FROM marker_areas")
    fun getSize(): Int

    @Query("SELECT * FROM markers INNER JOIN marker_areas ON markers.u_id=marker_areas.marker_id WHERE marker_areas.area_id=:areaId")
    fun getMarkersForArea(areaId: Long): List<Marker>

    @Query("SELECT * FROM areas INNER JOIN marker_areas ON areas.u_id = marker_areas.area_id WHERE marker_areas.marker_id=:markerId AND areas.`group` IN (:groups)")
    fun getAreasForMarker(markerId: Long, groups: Array<Int>): List<Area>

    @Query("DELETE FROM marker_areas")
    fun deleteAll()
}
