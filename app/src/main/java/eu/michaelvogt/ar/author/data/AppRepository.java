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
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AppRepository {
  private LocationDao locationDao;
  private MarkerDao markerDao;
  private AreaDao areaDao;

  private MarkerAreaDao markerAreaDao;

  private VisualDetailDao visualDetailDao;
  private EventDetailDao eventDetailDao;

  private LiveData<List<Location>> allLocations;

  AppRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    locationDao = db.locationDao();
    allLocations = locationDao.getAll();

    markerDao = db.markerDao();
    areaDao = db.areaDao();
    markerAreaDao = db.markerAreaDao();

    visualDetailDao = db.visualDetailDao();
    eventDetailDao = db.eventDetailDao();
  }

  public void insert(Location location) {
    DbTask.execute(() -> locationDao.insert(location));
  }

  public void insert(Marker marker) {
    DbTask.execute(() -> markerDao.insert(marker));
  }

  public void insert(Area area) {
    DbTask.execute(() -> areaDao.insert(area));
  }

  // Location
  LiveData<List<Location>> getAllLocations() {
    return allLocations;
  }

  LiveData<Location> getLocation(int uId) {
    return locationDao.get(uId);
  }

  public LiveData<Integer> getLocationsSize() {
    return locationDao.getSize();
  }


  // Marker
  LiveData<Marker> getMarker(int uId) {
    return markerDao.get(uId);
  }

  public void update(Location location) {
    DbTask.execute(() -> locationDao.update(location));
  }

  public void update(Marker marker) {
    DbTask.execute(() -> markerDao.update(marker));
  }

  @NotNull
  public LiveData<List<Marker>> allMarkers() {
    return markerDao.getAll();
  }

  @NotNull
  public LiveData<List<Marker>> getMarkersForLocation(int locationId, boolean withTitles) {
    if (withTitles) {
      return markerDao.findMarkersAndTitlesForLocation(locationId);
    } else {
      return markerDao.findMarkersOnlyForLocation(locationId);
    }
  }


  // Area
  CompletableFuture<Area> getArea(int uId) {
    return CompletableFuture.supplyAsync(() -> areaDao.get(uId));
  }

  LiveData<Area> getAreaWithTitle(String title) {
    return areaDao.findAreaWithTitle(title);
  }

  public CompletableFuture<List<Area>> getAreasForMarker(int markerId) {
    return CompletableFuture.supplyAsync(() -> markerAreaDao.getAreasForMarker(markerId));
  }

  // AreaVisual
  public CompletableFuture<AreaVisual> getAreaVisual(int areaId) {
    CompletableFuture<AreaVisual> futureAreaVisual = CompletableFuture.supplyAsync(() -> {
      CompletableFuture<Area> futureArea = getArea(areaId);
      CompletableFuture<List<VisualDetail>> futureDetails = getVisualDetailsForArea(areaId);
      CompletableFuture<List<EventDetail>> futureEvents = getVisualEventsForArea(areaId);

      return new AreaVisual(futureArea.join(), futureDetails.join(), futureEvents.join());
    });

    return futureAreaVisual;
  }

  public CompletableFuture<List<AreaVisual>> getAreaVisualsForMarker(int markerId) {
    List<AreaVisual> areaVisuals = new ArrayList<>();
    CompletableFuture<List<AreaVisual>> futureAreaVisuals = CompletableFuture.supplyAsync(() -> {
      getAreasForMarker(markerId).thenAccept(areas -> {
        areas.forEach(area -> {
          CompletableFuture<List<VisualDetail>> futureDetails = getVisualDetailsForArea(area.getUid());
          CompletableFuture<List<EventDetail>> futureEvents = getVisualEventsForArea(area.getUid());

          areaVisuals.add(new AreaVisual(area, futureDetails.join(), futureEvents.join()));
        });
      });
      return areaVisuals;
    });

    return futureAreaVisuals;
  }


  private CompletableFuture<List<VisualDetail>> getVisualDetailsForArea(int areaId) {
    return CompletableFuture.supplyAsync(() -> visualDetailDao.getForArea(areaId));
  }

  private CompletableFuture<List<EventDetail>> getVisualEventsForArea(int areaId) {
    return CompletableFuture.supplyAsync(() -> eventDetailDao.getForArea(areaId));
  }


  private static class DbTask<T> extends AsyncTask<Consumer<T>, Void, Void> {
    @Override
    @SafeVarargs
    protected final Void doInBackground(final Consumer<T>... params) {
      params[0].accept(null);
      return null;
    }
  }
}
