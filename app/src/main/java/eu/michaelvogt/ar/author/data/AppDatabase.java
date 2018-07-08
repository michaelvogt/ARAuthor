package eu.michaelvogt.ar.author.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Marker.class, Area.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  public abstract MarkerDao userDao();

  public abstract AreaDao areaDao();

  private static AppDatabase INSTANCE;

  public AppDatabase getDatabase(Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room
              .databaseBuilder(
                  context.getApplicationContext(), AppDatabase.class, "app_database")
              .build();
        }
      }
    }

    return INSTANCE;
  }
}