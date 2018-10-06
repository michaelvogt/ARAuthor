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
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "areas")
public class Area {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "u_id")
  private int uid;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "object_type")
  private int objectType;

  @ColumnInfo(name = "usage_type")
  private int usageType;

  public Area() {
    this(0, 0, "");
  }

  public Area(int opjectType, int usageType, String title) {
    this.objectType = opjectType;
    this.title = title;
    this.usageType = usageType;
  }

  @Ignore
  public Area(Area area) {
    this.objectType = area.getObjectType();
    this.usageType = area.getUsageType();
    this.title = area.getTitle();
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public int getObjectType() {
    return objectType;
  }

  public void setObjectType(int typeResource) {
    this.objectType = typeResource;
  }

  public int getUsageType() {
    return usageType;
  }

  public void setUsageType(int usageType) {
    this.usageType = usageType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
