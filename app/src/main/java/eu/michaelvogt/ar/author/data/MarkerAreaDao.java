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
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MarkerAreaDao extends BaseDao<MarkerArea> {
  @Insert
  long insert(MarkerArea markerArea);

  @Query("SELECT * FROM markers INNER JOIN marker_area ON " +
      "markers.u_id=marker_area.marker_id WHERE marker_area.area_id=:areaId")
  LiveData<List<Marker>> getMarkersForArea(int areaId);

  @Query("SELECT * FROM areas INNER JOIN marker_area ON " +
      "areas.u_id = marker_area.area_id WHERE marker_area.marker_id =:markerId")
  LiveData<List<Area>> getAreasForMarker(int markerId);

  @Query("DELETE FROM marker_area")
  void deleteAll();
}
