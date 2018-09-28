package eu.michaelvogt.ar.author.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AppRepository {
  private LocationDao locationDao;
  private LiveData<List<Location>> allLocations;

  AppRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    locationDao = db.locationDao();
    allLocations = locationDao.getAll();
  }

  LiveData<List<Location>> getAllLocations() {
    return allLocations;
  }

  public void insert(Location location) {
    new insertAsyncTask(locationDao).execute(location);
  }

  private static class insertAsyncTask extends AsyncTask<Location, Void, Void> {
    private LocationDao mAsyncTaskDao;

    insertAsyncTask(LocationDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(final Location... params) {
      mAsyncTaskDao.insert(params[0]);
      return null;
    }
  }
}
