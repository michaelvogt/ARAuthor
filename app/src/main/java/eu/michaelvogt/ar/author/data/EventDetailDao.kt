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
interface EventDetailDao : BaseDao<EventDetail> {
    @Query("SELECT * from event_detail where area_id=:areaId")
    fun getForArea(areaId: Long): List<EventDetail>

    @Query("DELETE FROM event_detail")
    fun deleteAll()
}
