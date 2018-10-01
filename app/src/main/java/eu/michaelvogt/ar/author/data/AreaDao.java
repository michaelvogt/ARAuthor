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

package eu.michaelvogt.ar.author.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AreaDao extends BaseDao<Area> {
  @Query("SELECT * from areas where areas.u_id=:uId")
  LiveData<Area> get(int uId);

  @Query("SELECT * from areas where areas.title=:title")
  LiveData<Area> findAreaWithTitle(String title);

  @Query("SELECT * from areas ORDER BY title ASC")
  LiveData<List<Area>> getAll();

  @Query("DELETE FROM areas")
  void deleteAll();

}
