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

@Dao
interface LocationDao : BaseDao<Location> {
    @Query("SELECT * FROM locations ORDER BY name ASC")
    fun getAll(): List<Location>

    @Query("SELECT u_id, name, is_default_location FROM locations ORDER BY name ASC")
    fun getNames(): List<Location>

    @Query("SELECT module_id FROM locations")
    fun getModuleIds(): List<String>

    @Query("SELECT COUNT(*) FROM locations")
    fun getSize(): Int

    @Query("SELECT * FROM locations WHERE u_id=:uId")
    fun get(uId: Long): Location

    @Query("DELETE FROM locations")
    fun deleteAll()

    @Query("DELETE FROM locations WHERE is_default_location = 0")
    fun deleteAllExceptMyLocation()

    @Query("SELECT * FROM locations WHERE name LIKE :name")
    fun findLocationByName(name: String): Location?

    @Query("SELECT * FROM  locations WHERE is_default_location = 1")
    fun findDefaultLocation(): Location?
}
