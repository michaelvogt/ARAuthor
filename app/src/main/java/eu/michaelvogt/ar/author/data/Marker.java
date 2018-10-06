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
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.ar.sceneform.math.Vector3;

@Entity(tableName = "markers",
    foreignKeys = @ForeignKey(entity = Location.class,
        parentColumns = "u_id",
        childColumns = "location_id"))
public class Marker {
  public static final int MIN_SIZE = 300;

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "u_id")
  private int uId;

  @ColumnInfo(name = "location_id")
  private int locationId;

  @ColumnInfo(name = "thumb_path")
  private String thumbPath;

  @ColumnInfo(name = "image_path")
  private String markerImagePath;

  @ColumnInfo(name = "background_image_path")
  private String backgroundImagePath;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "intro")
  private String intro;

  @ColumnInfo(name = "is_title")
  private boolean isTitle = false;

  @ColumnInfo(name = "location")
  private String location;

  @ColumnInfo(name = "width_in_m")
  private float widthInM = -1;

  @ColumnInfo(name = "size")
  private Vector3 size;

  @ColumnInfo(name = "zero_point")
  private Vector3 zeroPoint;

  @ColumnInfo(name = "show_background")
  private boolean showBackground;

  @Ignore
  public Marker() {
    this("", "", "", "", "", .0f,
        Vector3.zero(), Vector3.zero(), true, 0, false);
  }

  public Marker(String markerImagePath, String backgroundImagePath, String title, String intro, String location,
                float widthInM, Vector3 zeroPoint, Vector3 size, boolean showBackground, int locationId, boolean isTitle) {
    this.markerImagePath = markerImagePath;
    this.title = title;
    this.intro = intro;
    this.location = location;
    this.widthInM = widthInM;
    this.zeroPoint = zeroPoint;
    this.size = size;
    this.backgroundImagePath = backgroundImagePath;
    this.showBackground = showBackground;
    this.locationId = locationId;
    this.isTitle = isTitle;
    this.thumbPath = "";
  }

  @Ignore
  public Marker(@NonNull Marker marker) {
    this.markerImagePath = marker.getMarkerImagePath();
    this.title = marker.getTitle();
    this.isTitle = marker.isTitle();
    this.intro = marker.getIntro();
    this.location = marker.getLocation();
    this.widthInM = marker.getWidthInM();
    this.zeroPoint = getZeroPoint();
    this.size = marker.getSize();
    this.backgroundImagePath = marker.getBackgroundImagePath();
    this.showBackground = marker.isShowBackground();
    this.thumbPath = marker.getThumbPath();
    this.locationId = marker.getLocationId();
  }

  // TODO: Hack - Replace with Location object
  @Ignore
  public Marker(String title, int locationId) {
    this.title = title;
    this.isTitle = true;
    this.locationId = locationId;
  }

  public int getUId() {
    return uId;
  }

  public void setUId(@NonNull Integer uId) {
    this.uId = uId;
  }

  public void setThumbPath(String thumbPath) {
    this.thumbPath = thumbPath;
  }

  public String getThumbPath() {
    return thumbPath;
  }

  public String getMarkerImagePath() {
    return markerImagePath;
  }

  public String getTitle() {
    return title;
  }

  public String getIntro() {
    return intro;
  }

  public String getLocation() {
    return location;
  }

  public void setMarkerImagePath(String markerImagePath) {
    this.markerImagePath = markerImagePath;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setIntro(String intro) {
    this.intro = intro;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public boolean hasImage() {
    return !markerImagePath.isEmpty();
  }

  public float getWidthInM() {
    return widthInM;
  }

  public void setWidthInM(float widthInM) {
    this.widthInM = widthInM;
  }

  public Vector3 getSize() {
    return size;
  }

  public void setSize(Vector3 size) {
    this.size = size;
  }

  public Vector3 getZeroPoint() {
    return zeroPoint;
  }

  public void setZeroPoint(Vector3 zeroPoint) {
    this.zeroPoint = zeroPoint;
  }

  public String getBackgroundImagePath() {
    return backgroundImagePath;
  }

  public void setBackgroundImagePath(String backgroundImagePath) {
    this.backgroundImagePath = backgroundImagePath;
  }

  public boolean isShowBackground() {
    return showBackground;
  }

  public void setShowBackground(boolean showBackground) {
    this.showBackground = showBackground;
  }

  public boolean isTitle() {
    return isTitle;
  }

  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
  }

  public void setTitle(boolean title) {
    isTitle = title;
  }
}
