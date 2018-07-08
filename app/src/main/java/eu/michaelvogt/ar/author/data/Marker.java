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
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class Marker {
  public static final int MIN_SIZE = 300;

  @PrimaryKey(autoGenerate = true)
  @NonNull
  private int uid;

  @ColumnInfo(name = "thumb_path")
  private String thumbPath;

  @ColumnInfo(name = "image_path")
  private String imagePath;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "location")
  private String location;

  @ColumnInfo(name = "width_in_m")
  private float widthInM = -1;

  @Ignore
  private ArrayList<String> areaIds;

  public Marker() {
    this("", "", "", .03f, new ArrayList<>());
  }

  public Marker(String imagePath, String title, String location, float widthInM, ArrayList<String> areas) {
    this.imagePath = imagePath;
    this.title = title;
    this.location = location;
    this.widthInM = widthInM;
    this.thumbPath = "";
    this.areaIds = areas;
  }

  public Marker(Marker marker) {
    this.imagePath = marker.getImagePath();
    this.title = marker.getTitle();
    this.location = marker.getLocation();
    this.widthInM = marker.getWidthInM();
    this.thumbPath = "";
    this.areaIds = marker.getAreaIds();
  }

  @NonNull
  public int getUid() {
    return uid;
  }

  public void setUid(@NonNull int uid) {
    this.uid = uid;
  }

  public void setThumbPath(String thumbPath) {
    this.thumbPath = thumbPath;
  }

  public String getThumbPath() {
    return thumbPath;
  }

  public String getImagePath() {
    return imagePath;
  }

  public String getTitle() {
    return title;
  }

  public String getLocation() {
    return location;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public boolean hasImage() {
    return !imagePath.isEmpty();
  }

  public float getWidthInM() {
    return widthInM;
  }

  public void setWidthInM(float widthInM) {
    this.widthInM = widthInM;
  }

  public ArrayList<String> getAreaIds() {
    return areaIds;
  }

  public String getAreaId(int index) {
    return areaIds.get(index);
  }

  public void setAreaIds(ArrayList<String> areaIds) {
    this.areaIds = areaIds;
  }
}
