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
import android.arch.persistence.room.PrimaryKey;

import eu.michaelvogt.ar.author.utils.Converter;

@Entity(tableName = "event_detail",
    foreignKeys = @ForeignKey(entity = Area.class,
        parentColumns = "u_id",
        childColumns = "area_id"))
public class EventDetail {
  private static final String TAG = EventDetail.class.getSimpleName();

  public static final int EVENT_SWITCHLANGUAGE = 1;
  public static final int EVENT_GRABCONTENT = 2;
  public static final int EVENT_ZOOM = 3;
  public static final int EVENT_SCALE = 4;
  public static final int EVENT_HIDECONTENT = 5;
  public static final int EVENT_SETMAINCONTENT = 6;

  public static final int TYPE_ALL = 0;

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "u_id")
  private int uId;

  @ColumnInfo(name = "area_id")
  private int areaId;

  @ColumnInfo(name = "type")
  private int type;

  @ColumnInfo(name = "target")
  private int target;

  @ColumnInfo(name = "value")
  public String value;

  public EventDetail() {
    this(0, 0, 0, null);
  }

  public EventDetail(int areaId, int type, int target, Object value) {
    this.areaId = areaId;
    this.type = type;
    this.target = target;
    this.value = Converter.stringify(value);
  }

  public int getUId() {
    return uId;
  }

  public void setUId(int uId) {
    this.uId = uId;
  }

  public int getAreaId() {
    return areaId;
  }

  public void setAreaId(int areaId) {
    this.areaId = areaId;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getTarget() {
    return target;
  }

  public void setTarget(int target) {
    this.target = target;
  }

  public Object getValue() {
    return Converter.objectify(value);
  }

  public void setValue(Object value) {
    this.value = Converter.stringify(value);
  }
}
