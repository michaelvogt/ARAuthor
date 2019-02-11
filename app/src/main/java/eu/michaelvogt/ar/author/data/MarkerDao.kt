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

import androidx.room.Dao
import androidx.room.Query
import eu.michaelvogt.ar.author.data.tuples.ListMarker

@Dao
interface MarkerDao : BaseDao<Marker> {

    @Query("SELECT * FROM markers")
    fun getAll(): List<Marker>

    @Query("SELECT u_id as markerId, title, intro, image_path as imagePath FROM markers WHERE group_id=:groupId")
    fun getAllForGroup(groupId: Long): List<ListMarker>

    @Query("SELECT u_id as markerId, title, intro, image_path as imagePath FROM markers WHERE group_id IS NULL")
    fun getAllWithoutGroup(): List<ListMarker>

    @Query("SELECT u_id as markerId, title, intro, image_path as imagePath FROM markers WHERE location_id=:locationId AND group_id IS null")
    fun getAllWithoutGroupForLocation(locationId: Long): List<ListMarker>

    @Query("SELECT * FROM markers WHERE markers.u_id=:uId")
    fun get(uId: Long): Marker

    @Query("SELECT markers.u_id FROM markers INNER JOIN title_groups ON markers.group_id=title_groups.u_id WHERE markers.title like '看板' AND title_groups.name like '宗岡家'")
    fun getIdFromGroup(): Long

    @Query("SELECT COUNT(*) FROM markers")
    fun getSize(): Int

    @Query("SELECT * FROM markers WHERE title LIKE :title")
    fun findMarkerByTitle(title: String): Marker

    @Query("SELECT u_id as markerId, title, intro, image_path as imagePath FROM markers WHERE location_id=:locationId AND group_id=:groupId")
    fun getGroupForLocation(locationId: Long, groupId: Long): List<ListMarker>

    @Query("SELECT * FROM markers WHERE location_id=:locationId")
    fun findMarkersForLocation(locationId: Long): List<Marker>

    @Query("DELETE FROM markers")
    fun deleteAll()
}
