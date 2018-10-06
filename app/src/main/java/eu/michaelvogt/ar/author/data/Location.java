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
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(
    tableName = "locations",
    indices = @Index(value = {"name"}, unique = true))
public class Location {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "u_id")
  private int uId;

  @NonNull
  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "thumb_path")
  private String thumbPath;

  @ColumnInfo(name = "intro_html_path")
  private String introHtmlPath;

  @Ignore
  public Location() {
    this("", "", "");
  }

  public Location(@NonNull String name, String thumbPath, String introHtmlPath) {
    this.name = name;
    this.thumbPath = thumbPath;
    this.introHtmlPath = introHtmlPath;
  }

  public int getUId() {
    return uId;
  }

  public void setUId(int uId) {
    this.uId = uId;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public String getThumbPath() {
    return thumbPath;
  }

  public String getIntroHtmlPath() {
    return introHtmlPath;
  }

  public void setThumbPath(String thumbPath) {
    this.thumbPath = thumbPath;
  }

  public void setIntroHtmlPath(String introHtmlPath) {
    this.introHtmlPath = introHtmlPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Location location = (Location) o;

//    if (uId != location.uId) return false;
    if (!name.equals(location.name)) return false;
    if (thumbPath != null ? !thumbPath.equals(location.thumbPath) : location.thumbPath != null)
      return false;
    return introHtmlPath != null ? introHtmlPath.equals(location.introHtmlPath) : location.introHtmlPath == null;
  }

  @Override
  public int hashCode() {
    int result = uId;
    result = 31 * result + name.hashCode();
    result = 31 * result + (thumbPath != null ? thumbPath.hashCode() : 0);
    result = 31 * result + (introHtmlPath != null ? introHtmlPath.hashCode() : 0);
    return result;
  }
}
