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

import android.app.Application
import android.arch.lifecycle.LiveData
import org.jetbrains.anko.doAsync
import java.util.*
import java.util.concurrent.CompletableFuture

class AppRepository internal constructor(application: Application) {
    private val locationDao: LocationDao
    private val markerDao: MarkerDao
    private val areaDao: AreaDao

    private val markerAreaDao: MarkerAreaDao

    private val visualDetailDao: VisualDetailDao
    private val eventDetailDao: EventDetailDao

    // Location
    internal val allLocations: LiveData<List<Location>>

    val locationsSize: LiveData<Int>
        get() = locationDao.getSize()

    init {
        val db = AppDatabase.getDatabase(application)
        locationDao = db!!.locationDao()
        allLocations = locationDao.getAll()

        markerDao = db.markerDao()
        areaDao = db.areaDao()
        markerAreaDao = db.markerAreaDao()

        visualDetailDao = db.visualDetailDao()
        eventDetailDao = db.eventDetailDao()
    }

    fun insert(location: Location) {
        doAsync { locationDao.insert(location) }
    }

    fun insert(marker: Marker) {
        doAsync { markerDao.insert(marker) }
    }

    fun insert(area: Area) {
        doAsync { areaDao.insert(area) }
    }

    internal fun getLocation(uId: Long): LiveData<Location> {
        return locationDao.get(uId)
    }


    // Marker
    internal fun getMarker(uId: Long): LiveData<Marker> {
        return markerDao.get(uId)
    }

    fun update(location: Location) {
        doAsync { locationDao.update(location) }
    }

    fun update(marker: Marker) {
        doAsync { markerDao.update(marker) }
    }

    fun allMarkers(): LiveData<List<Marker>> {
        return markerDao.getAll()
    }

    fun getMarkersForLocation(locationId: Long, withTitles: Boolean): LiveData<List<Marker>> {
        return if (withTitles) {
            markerDao.findMarkersAndTitlesForLocation(locationId)
        } else {
            markerDao.findMarkersOnlyForLocation(locationId)
        }
    }


    // Area
    internal fun getArea(uId: Long): CompletableFuture<Area> {
        return CompletableFuture.supplyAsync { areaDao.get(uId) }
    }

    internal fun getAreaWithTitle(title: String): LiveData<Area> {
        return areaDao.findAreaByTitle(title)
    }

    fun getAreasForMarker(markerId: Long): CompletableFuture<List<Area>> {
        return CompletableFuture.supplyAsync { markerAreaDao.getAreasForMarker(markerId) }
    }

    // AreaVisual
    fun getAreaVisual(areaId: Long): CompletableFuture<AreaVisual> {
        return CompletableFuture.supplyAsync {
            val futureArea = areaDao.get(areaId)
            val futureDetails = visualDetailDao.getForArea(areaId)
            val futureEvents = eventDetailDao.getForArea(areaId)

            AreaVisual(futureArea, futureDetails, futureEvents)
        }
    }

    fun getAreaVisualsForMarker(markerId: Long): CompletableFuture<ArrayList<AreaVisual>> {
        val areaVisuals = ArrayList<AreaVisual>()

        return CompletableFuture.supplyAsync {
            val areas = markerAreaDao.getAreasForMarker(markerId)
            areas.forEach { area ->
                val details = visualDetailDao.getForArea(area.uId)
                val events = eventDetailDao.getForArea(area.uId)

                areaVisuals.add(AreaVisual(area, details, events))
            }

            areaVisuals
        }
    }
}
