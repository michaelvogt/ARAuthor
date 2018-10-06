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
public interface LocationDao extends BaseDao<Location> {
  @Query("SELECT * from locations where u_id=:uId")
  LiveData<Location> get(int uId);

  @Query("SELECT * from locations ORDER BY name ASC")
  LiveData<List<Location>> getAll();

  @Query("SELECT COUNT(*) FROM locations")
  int getSize();

  @Query("DELETE FROM locations")
  void deleteAll();

  @Query("Select * from locations where name like :name")
  Location findLocationByName(String name);
}
