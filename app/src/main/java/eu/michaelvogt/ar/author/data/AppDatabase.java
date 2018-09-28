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

import com.google.ar.sceneform.math.Vector3;

import eu.michaelvogt.ar.author.data.utils.Converters;

@Database(entities = {Location.class, Marker.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
  public abstract LocationDao locationDao();
  public abstract MarkerDao markerDao();

  private static AppDatabase INSTANCE;

  public static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room
              .databaseBuilder(
                  context.getApplicationContext(), AppDatabase.class, "app_database")
              .addCallback(sRoomDatabaseCallback)
              .build();
        }
      }
    }

    return INSTANCE;
  }

  private static AppDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

    @Override
    public void onOpen (@NonNull SupportSQLiteDatabase db){
      super.onOpen(db);
      new PopulateDbAsync(INSTANCE).execute();
    }
  };

  private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final LocationDao locationDao;
    private final MarkerDao markerDao;

    PopulateDbAsync(AppDatabase db) {
      locationDao = db.locationDao();
      markerDao = db.markerDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
      locationDao.deleteAll();

      long locationId = insertLocation(new Location(
          "石見銀山",
          "Touristar/iwamiginzan/images/igk_machinami.jpg",
          "Touristar/iwamiginzan/intro.html"));
          // add markers for this location

      insertMarker(new Marker("城上神社"));
      insertMarker(new Marker(
          "/Touristar/Markers/IMG_20180522_105701.jpg",
          "",
          "看板",
          "拝殿正面は一〇、ニニメートル、側面は二、八八メートル。屋根は重曹式の入母屋造り瓦葺きで江戸の亀戸天満宮を天本にしたものと伝えています。",
          "城上神社",
          0.6f,
          Vector3.zero(),
          new Vector3(1.4f, 0.005f, 0.715f),
          false));

      locationId = insertLocation(new Location(
          "Hakodate",
          "Touristar/hakodate/images/goryokakumainhall.jpg",
          "Touristar/hakodate/intro.html"));
          // add markers for this location

      return null;
    }

    private long insertLocation(Location location) {
      return locationDao.insert(location);
    }

    private long insertMarker(Marker marker) {
      return markerDao.insert(marker);
    }
  }
}