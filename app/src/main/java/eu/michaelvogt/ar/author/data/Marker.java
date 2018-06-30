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

import java.util.ArrayList;

public class Marker {
  public static final int MIN_SIZE = 300;

  private String thumbPath;
  private String imagePath;
  private String title;
  private String location;
  private float widthInM = -1;
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
