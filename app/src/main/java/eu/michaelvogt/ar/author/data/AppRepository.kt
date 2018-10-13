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
            markerDao.findMarkersAndTitlesForLocation(locationId.toLong())
        } else {
            markerDao.findMarkersOnlyForLocation(locationId.toLong())
        }
    }


    // Area
    internal fun getArea(uId: Int): CompletableFuture<Area> {
        return CompletableFuture.supplyAsync { areaDao.get(uId) }
    }

    internal fun getAreaWithTitle(title: String): LiveData<Area> {
        return areaDao.findAreaByTitle(title)
    }

    fun getAreasForMarker(markerId: Long): CompletableFuture<List<Area>> {
        return CompletableFuture.supplyAsync { markerAreaDao.getAreasForMarker(markerId) }
    }

    // AreaVisual
    fun getAreaVisual(areaId: Int): CompletableFuture<AreaVisual> {

        return CompletableFuture.supplyAsync {
            val futureArea = getArea(areaId)
            val futureDetails = getVisualDetailsForArea(areaId.toLong())
            val futureEvents = getVisualEventsForArea(areaId.toLong())

            AreaVisual(futureArea.join(), futureDetails.join(), futureEvents.join())
        }
    }

    fun getAreaVisualsForMarker(markerId: Long): CompletableFuture<List<AreaVisual>> {
        val areaVisuals = ArrayList<AreaVisual>()

        return CompletableFuture.supplyAsync {
            getAreasForMarker(markerId).thenAccept { areas ->
                areas.forEach { area ->
                    val futureDetails = getVisualDetailsForArea(area.uId)
                    val futureEvents = getVisualEventsForArea(area.uId)

                    areaVisuals.add(AreaVisual(area, futureDetails.join(), futureEvents.join()))
                }
            }

            areaVisuals
        }
    }


    private fun getVisualDetailsForArea(areaId: Long): CompletableFuture<List<VisualDetail>> {
        return CompletableFuture.supplyAsync { visualDetailDao.getForArea(areaId) }
    }

    private fun getVisualEventsForArea(areaId: Long): CompletableFuture<List<EventDetail>> {
        return CompletableFuture.supplyAsync { eventDetailDao.getForArea(areaId) }
    }
}
