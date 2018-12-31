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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface SlideDao : BaseDao<Slide> {
    @Query("SELECT * from slides")
    fun getAll(): LiveData<List<Slide>>

    @Query("SELECT COUNT(*) FROM slides")
    fun getSize(): LiveData<Int>

    @Query("SELECT * from slides where detail_id=:detailId ORDER BY `order` ASC")
    fun getSlidesForDetail(detailId: Long): List<Slide>

    @Query("DELETE FROM slides")
    fun deleteAll()
}