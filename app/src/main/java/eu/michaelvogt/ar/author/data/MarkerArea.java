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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "marker_area",
    primaryKeys = {"marker_id", "area_id"},
    foreignKeys = {
        @ForeignKey(entity = Marker.class,
            parentColumns = "u_id",
            childColumns = "marker_id"),
        @ForeignKey(entity = Area.class,
            parentColumns = "u_id",
            childColumns = "area_id")
    })
public class MarkerArea {
  @ColumnInfo(name = "marker_id")
  private int markerId;

  @ColumnInfo(name = "area_id")
  private int areaId;

  public MarkerArea(final int markerId, final int areaId) {
    this.markerId = markerId;
    this.areaId = areaId;
  }

  public int getAreaId() {
    return areaId;
  }

  public void setAreaId(int areaId) {
    this.areaId = areaId;
  }

  public int getMarkerId() {
    return markerId;
  }

  public void setMarkerId(int markerId) {
    this.markerId = markerId;
  }
}
