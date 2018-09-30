package eu.michaelvogt.ar.author.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class AppRepository {
  private LocationDao locationDao;
  private MarkerDao markerDao;

  private LiveData<List<Location>> allLocations;

  AppRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    locationDao = db.locationDao();
    allLocations = locationDao.getAll();

    markerDao = db.markerDao();
  }

  // Location
  LiveData<List<Location>> getAllLocations() {
    return allLocations;
  }

  LiveData<Location> getLocation(int uId) {
    return locationDao.get(uId);
  }

  int getLocationsSize() {
    return locationDao.getSize();
  }

  // Marker
  LiveData<Marker> getMarker(int uId) {
    return markerDao.get(uId);
  }

  public void insert(Location location) {
    DbTask.execute(() -> locationDao.insert(location));
  }

  public void insert(Marker marker) {
    DbTask.execute(() -> markerDao.insert(marker));
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
    return markerDao.findMarkersForLocation(locationId, withTitles);
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
