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

package eu.michaelvogt.ar.author.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.os.AsyncTask

import eu.michaelvogt.ar.author.data.utils.Converters
import eu.michaelvogt.ar.author.data.utils.DatabaseInitializer

@Database(entities = [Location::class, Marker::class, Area::class, MarkerArea::class,
    VisualDetail::class, EventDetail::class], version = 12)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun markerDao(): MarkerDao
    abstract fun areaDao(): AreaDao

    abstract fun markerAreaDao(): MarkerAreaDao

    abstract fun visualDetailDao(): VisualDetailDao
    abstract fun eventDetailDao(): EventDetailDao

    private class PopulateDbAsync internal constructor(private val db: AppDatabase) : AsyncTask<Void, Void, Void>() {
        private val locationDao: LocationDao = db.locationDao()
        private val markerDao: MarkerDao = db.markerDao()
        private val areaDao: AreaDao = db.areaDao()

        private val markerAreaDao: MarkerAreaDao = db.markerAreaDao()

        private val visualDetailDao: VisualDetailDao = db.visualDetailDao()
        private val eventDetailDao: EventDetailDao = db.eventDetailDao()

        override fun doInBackground(vararg params: Void): Void? {
            db.clearAllTables()

            DatabaseInitializer.runner(locationDao, markerDao, areaDao, markerAreaDao, visualDetailDao).run()

            return null
        }
    }

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room
                                .databaseBuilder(
                                        context.applicationContext, AppDatabase::class.java, "app_database")
                                .fallbackToDestructiveMigration()
                                .addCallback(sRoomDatabaseCallback)
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                PopulateDbAsync(INSTANCE!!).execute()
            }
        }
    }
}