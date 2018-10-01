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
public interface MarkerDao extends BaseDao<Marker> {
  @Query("SELECT * from markers where markers.u_id=:uId")
  LiveData<Marker> get(int uId);

  @Query("SELECT * from markers ORDER BY title ASC")
  LiveData<List<Marker>> getAll();

  @Query("SELECT * FROM markers WHERE location_id=:locationId AND is_title=0")
  LiveData<List<Marker>> findMarkersWithoutTitlesForLocation(final int locationId);

  @Query("SELECT * FROM markers WHERE location_id=:locationId")
  LiveData<List<Marker>> findMarkersWithTitlesForLocation(final int locationId);

  @Query("DELETE FROM markers")
  void deleteAll();
}
