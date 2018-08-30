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

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import eu.michaelvogt.ar.author.utils.Event;

public class AuthorViewModel extends AndroidViewModel {
  private final List<Marker> markers = new ArrayList<>();
  private final List<Area> areas = new ArrayList<>();
  private final List<Event> events = new ArrayList<>();

  private Marker cropMarker = null;

  public AuthorViewModel(Application application) {
    super(application);
  }

  public void addMarker(Marker marker) {
    markers.add(marker);
  }

  public Marker getEditMarker(int index) {
    return new Marker(markers.get(index));
  }

  public Marker getMarker(int index) {
    return markers.get(index);
  }

  public Optional<Marker> getMarkerFromString(String name) {
    return markers.stream().filter(marker -> marker.getTitle().equals(name)).findFirst();
  }

  public Optional<Marker> getMarkerFromUid(int id) {
    return markers.stream().filter(marker -> marker.getUid() == id).findFirst();
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

  public void addArea(Area area) {
    areas.add(area);
  }

  public Area getArea(int index) {
    return areas.get(index);
  }

  public Optional<Area> getArea(String areaId) {
    return areas.stream().filter(area -> area.getTitle().equals(areaId)).findFirst();
  }

  public int getAreaSize() {
    return areas.size();
  }

  public void addEvent(Event event) {
    events.add(event);
  }

  public List<Event> getEvents(int key) {
    return events.stream().filter(event -> event.getKey() == key).collect(Collectors.toList());
  }
}
