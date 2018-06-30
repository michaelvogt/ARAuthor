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

import android.arch.lifecycle.ViewModel;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorViewModel extends ViewModel {
  private final List<Marker> markers = new ArrayList<>();
  private final List<Area> areas = new ArrayList<>();

  private Marker cropMarker = null;

  public void addMarker(Marker marker) {
    markers.add(marker);
  }

  public Marker getEditMarker(int index) {
    return new Marker(markers.get(index));
  }

  public Marker getMarker(int index) {
    return markers.get(index);
  }

  public void setMarker(int index, Marker marker) {
    markers.set(index, marker);
  }

  public int getMarkerSize() {
    return markers.size();
  }

  public Iterable<Marker> markerIterable() {
    return markers;
  }

  public void setCropMarker(Marker editMarker) {
    cropMarker = editMarker;
  }

  public Marker getCropMarker() {
    return cropMarker;
  }

  public void clearCropMarker() {
    cropMarker = null;
  }

  public void addArea(Area area, Vector3 vector3, Quaternion quaternion) {
    areas.add(area);
  }

  public Area getArea(int index) {
    return areas.get(index) ;
  }

  public Optional<Area> getArea(String areaId) {
    return areas.stream().filter(area -> area.getTitle().equals(areaId)).findFirst();
  }

  public int getAreaSize() {
    return areas.size();
  }
}
