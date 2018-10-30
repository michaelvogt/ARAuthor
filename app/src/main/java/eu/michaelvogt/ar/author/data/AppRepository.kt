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

import org.jetbrains.anko.doAsync
import java.util.*
import java.util.concurrent.CompletableFuture

class AppRepository internal constructor(db: AppDatabase?) {
    private val locationDao: LocationDao
    private val markerDao: MarkerDao
    private val areaDao: AreaDao

    private val markerAreaDao: MarkerAreaDao

    private val visualDetailDao: VisualDetailDao
    private val eventDetailDao: EventDetailDao

    private val slideDao: SlideDao

    init {
        locationDao = db!!.locationDao()

        markerDao = db.markerDao()
        areaDao = db.areaDao()
        markerAreaDao = db.markerAreaDao()

        visualDetailDao = db.visualDetailDao()
        eventDetailDao = db.eventDetailDao()

        slideDao = db.slideDao()
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

    fun update(location: Location) {
        doAsync { locationDao.update(location) }
    }

    fun update(marker: Marker) {
        doAsync { markerDao.update(marker) }
    }

    fun update(area: Area) {
        doAsync { areaDao.update(area) }
    }

    // Location
    fun getLocation(uId: Long): CompletableFuture<Location> {
        return CompletableFuture.supplyAsync { locationDao.get(uId) }
    }

    fun allLocations(): CompletableFuture<List<Location>> {
        return CompletableFuture.supplyAsync { locationDao.getAll() }
    }


    // Marker
    fun getMarker(uId: Long): CompletableFuture<Marker> {
        return CompletableFuture.supplyAsync { markerDao.get(uId) }
    }

    fun allMarkers(): CompletableFuture<List<Marker>> {
        return CompletableFuture.supplyAsync { markerDao.getAll() }
    }

    fun getMarkersForLocation(locationId: Long, withTitles: Boolean): CompletableFuture<List<Marker>> {
        return if (withTitles) {
            CompletableFuture.supplyAsync { markerDao.findMarkersAndTitlesForLocation(locationId) }
        } else {
            CompletableFuture.supplyAsync { markerDao.findMarkersOnlyForLocation(locationId) }
        }
    }


    // Area
    internal fun getArea(uId: Long): CompletableFuture<Area> {
        return CompletableFuture.supplyAsync { areaDao.get(uId) }
    }

    internal fun getAreaWithTitle(title: String): CompletableFuture<Area> {
        return CompletableFuture.supplyAsync { areaDao.findAreaByTitle(title) }
    }

    fun getAreasForMarker(markerId: Long, group: Int): CompletableFuture<List<Area>> {
        return when (group) {
            -1 -> CompletableFuture.supplyAsync { markerAreaDao.getAreasForMarker(markerId) }
            else -> CompletableFuture.supplyAsync { markerAreaDao.getAreaGroupForMarker(markerId, group) }
        }
    }

    // AreaVisual
    fun getAreaVisual(areaId: Long): CompletableFuture<AreaVisual> {
        return CompletableFuture.supplyAsync {
            val area = areaDao.get(areaId)
            setupAreaVisual(area)
        }
    }

    fun getAreaVisualsForMarker(markerId: Long, group: Int): CompletableFuture<ArrayList<AreaVisual>> {
        val areaVisuals = ArrayList<AreaVisual>()

        return CompletableFuture.supplyAsync {
            val areas = markerAreaDao.getAreaGroupForMarker(markerId, group)
            areas.forEach { areaVisuals.add(setupAreaVisual(it)) }

            areaVisuals
        }
    }

    private fun setupAreaVisual(area: Area): AreaVisual {
        val details = visualDetailDao.getForArea(area.uId)
        val events = eventDetailDao.getForArea(area.uId)

        val slideDetails = details.filter { detail -> detail.key == KEY_SLIDES }
        slideDetails.forEach { detail -> detail.slides = slideDao.getSlidesForDetail(detail.uId) }

        return AreaVisual(area, details, events)
    }
}
