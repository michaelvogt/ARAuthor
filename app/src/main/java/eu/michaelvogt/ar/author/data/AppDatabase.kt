/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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

package eu.michaelvogt.ar.author.data

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.michaelvogt.ar.author.data.utils.Converters

@Database(entities = [Location::class, Marker::class, Area::class, MarkerArea::class,
    VisualDetail::class, EventDetail::class, Slide::class, TitleGroup::class], version = 21)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun markerDao(): MarkerDao
    abstract fun areaDao(): AreaDao

    abstract fun markerAreaDao(): MarkerAreaDao
    abstract fun titleGroupDao(): TitleGroupDao

    abstract fun visualDetailDao(): VisualDetailDao
    abstract fun eventDetailDao(): EventDetailDao

    abstract fun slideDao(): SlideDao

    // TODO: Replace with proper coroutine
    class PopulateDbAsync internal constructor(
            db: AppDatabase,
            val callback: () -> Unit) : AsyncTask<Void, Void, Void>() {
        private val locationDao: LocationDao = db.locationDao()
        private val markerDao: MarkerDao = db.markerDao()
        private val areaDao: AreaDao = db.areaDao()

        private val markerAreaDao: MarkerAreaDao = db.markerAreaDao()
        private val titleGroupDao: TitleGroupDao = db.titleGroupDao()

        private val visualDetailDao: VisualDetailDao = db.visualDetailDao()
        private val eventDetailDao: EventDetailDao = db.eventDetailDao()

        private val slideDao: SlideDao = db.slideDao()

        override
        fun doInBackground(vararg params: Void): Void? {
            // Delete all but 'my location' locations from the db until app is developed further
            // TODO: implement migration
            slideDao.deleteAll()
            eventDetailDao.deleteAll()
            visualDetailDao.deleteAll()
            markerAreaDao.deleteAll()
            areaDao.deleteAll()
            markerDao.deleteAll()
            locationDao.deleteAllExceptMyLocation()
            titleGroupDao.deleteAll()

            if (locationDao.findDefaultLocation() == null) {
                locationDao.insert(Location.getDefaultLocation())
            }

            return null
        }

        override
        fun onPostExecute(result: Void?) {
            callback()
        }
    }

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun createDatabase(context: Context) {

            Log.i("AppDatabase", "Create database")

            synchronized(AppDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(
                                    context.applicationContext, AppDatabase::class.java, "app_database")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
        }

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                createDatabase(context)
            }
            return INSTANCE
        }
    }
}