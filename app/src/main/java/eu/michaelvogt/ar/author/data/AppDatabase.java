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

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import eu.michaelvogt.ar.author.data.utils.Converters;
import eu.michaelvogt.ar.author.data.utils.DatabaseInitializer;

@Database(
    entities = {Location.class, Marker.class, Area.class, MarkerArea.class,
        VisualDetail.class, EventDetail.class},
    version = 6)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
  public abstract LocationDao locationDao();

  public abstract MarkerDao markerDao();

  public abstract AreaDao areaDao();

  public abstract MarkerAreaDao markerAreaDao();

  public abstract VisualDetailDao visualDetailDao();

  public abstract EventDetailDao eventDetailDao();

  private static AppDatabase INSTANCE;

  static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room
              .databaseBuilder(
                  context.getApplicationContext(), AppDatabase.class, "app_database")
              .fallbackToDestructiveMigration()
              .addCallback(sRoomDatabaseCallback)
              .build();
        }
      }
    }
    return INSTANCE;
  }

  private static AppDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
      super.onOpen(db);

      new PopulateDbAsync(INSTANCE).execute();
    }
  };

  private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final LocationDao locationDao;
    private final MarkerDao markerDao;
    private final AreaDao areaDao;

    private final MarkerAreaDao markerAreaDao;

    private final VisualDetailDao visualDetailDao;
    private final EventDetailDao eventDetailDao;

    PopulateDbAsync(AppDatabase db) {
      locationDao = db.locationDao();
      markerDao = db.markerDao();
      areaDao = db.areaDao();

      markerAreaDao = db.markerAreaDao();

      visualDetailDao = db.visualDetailDao();
      eventDetailDao = db.eventDetailDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
      markerAreaDao.deleteAll();
      areaDao.deleteAll();
      markerDao.deleteAll();
      locationDao.deleteAll();

      DatabaseInitializer.runner(locationDao, markerDao, areaDao, markerAreaDao).run();

      return null;
    }
  }
}