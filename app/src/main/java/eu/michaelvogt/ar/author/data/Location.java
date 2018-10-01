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

import java.util.List;

@Entity(tableName = "locations")
public class Location {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "u_id")
  private int uId;

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

  public Location(String name, String thumbPath, String introHtmlPath) {
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
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
}
