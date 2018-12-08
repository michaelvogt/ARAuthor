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

import android.util.Log
import eu.michaelvogt.ar.author.data.tuples.ListMarker
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_LOCATION
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_MARKER
import org.jetbrains.anko.doAsyncResult
import java.util.*
import java.util.concurrent.CompletableFuture

class AppRepository internal constructor(db: AppDatabase?) {
    private val locationDao: LocationDao
    private val markerDao: MarkerDao
    private val areaDao: AreaDao

    private val markerAreaDao: MarkerAreaDao
    private val titleGroupDao: TitleGroupDao

    private val visualDetailDao: VisualDetailDao
    private val eventDetailDao: EventDetailDao

    private val slideDao: SlideDao

    init {
        locationDao = db!!.locationDao()

        markerDao = db.markerDao()
        areaDao = db.areaDao()
        markerAreaDao = db.markerAreaDao()
        titleGroupDao = db.titleGroupDao()

        visualDetailDao = db.visualDetailDao()
        eventDetailDao = db.eventDetailDao()

        slideDao = db.slideDao()
    }

    fun insert(location: Location): CompletableFuture<Long> {
        return CompletableFuture.supplyAsync { locationDao.insert(location) }
    }

    fun insert(marker: Marker): CompletableFuture<Long> {
        return CompletableFuture.supplyAsync { markerDao.insert(marker) }
    }

    fun insert(area: Area): CompletableFuture<Long> {
        return CompletableFuture.supplyAsync { areaDao.insert(area) }
    }


    fun update(location: Location): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync { locationDao.update(location) }
    }

    fun update(marker: Marker): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync { markerDao.update(marker) }
    }

    fun update(area: Area): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync { areaDao.update(area) }
    }


    fun delete(location: Location): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync { locationDao.delete(location) }
    }

    fun delete(marker: Marker): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync { markerDao.delete(marker) }
    }

    fun delete(area: Area): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync { areaDao.delete(area) }
    }

    fun delete(areaVisual: AreaVisual): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync {
            markerAreaDao.deleteAreaRelations(areaVisual.area.uId)
            areaDao.delete(areaVisual.area)

            areaVisual.events.forEach(action = { index, eventDetail ->
                eventDetailDao.delete(eventDetail)
            })

            areaVisual.details.forEach { index, visualDetail ->
                visualDetailDao.delete(visualDetail)
            }
        }
    }

    // Location
    fun getLocation(uId: Long): CompletableFuture<Location> {
        return CompletableFuture.supplyAsync { locationDao.get(uId) }
    }

    fun allLocations(): CompletableFuture<List<Location>> {
        return CompletableFuture.supplyAsync { locationDao.getAll() }
    }

    fun getLocationNames(): CompletableFuture<List<Location>> {
        return CompletableFuture.supplyAsync { locationDao.getNames() }
    }


    // Marker
    fun getMarker(uId: Long): CompletableFuture<Marker> {
        return CompletableFuture.supplyAsync { markerDao.get(uId) }
    }

    fun allMarkers(): CompletableFuture<List<Marker>> {
        return CompletableFuture.supplyAsync { markerDao.getAll() }
    }

    fun getMarkersForLocation(locationId: Long): CompletableFuture<List<Marker>> {
        return when (locationId) {
            NEW_CURRENT_LOCATION -> CompletableFuture.supplyAsync { markerDao.getAll() }
            else -> CompletableFuture.supplyAsync { markerDao.findMarkersForLocation(locationId) }
        }
    }

    fun getMarkerGroupsForLocation(locationId: Long): List<ListMarker> {
        var listMarkers: List<ListMarker> = emptyList()

        val groups = CompletableFuture<List<ListMarker>>().doAsyncResult { titleGroupDao.getAll() }.get()

        // Get the markers added to groups
        groups.forEach {
            val markers = CompletableFuture<List<ListMarker>>().doAsyncResult {
                when (locationId) {
                    NEW_CURRENT_LOCATION -> markerDao.getAllForGroup(it.uId)
                    else -> markerDao.getGroupForLocation(locationId, it.uId)
                }
            }.get()

            if (markers.isNotEmpty()) {
                listMarkers += ListMarker(0L, it.name, isTitle = true)
                listMarkers += markers
            }
        }

        // Get the markers not added to a group
        val markers = CompletableFuture<List<ListMarker>>().doAsyncResult {
            when (locationId) {
                NEW_CURRENT_LOCATION -> markerDao.getAllWithoutGroup()
                else -> markerDao.getAllWithoutGroupForLocation(locationId)
            }
        }.get()

        if (markers.isNotEmpty()) {
            if (groups.isNotEmpty())
                listMarkers += ListMarker(0L, "@string/repo_marker_name_no_group", isTitle = true)

            listMarkers += markers
        }

        return listMarkers
    }

    fun getMarkerIdFromGroup(markerTitle: String, groupName: String): CompletableFuture<Long> {
        return CompletableFuture.supplyAsync { markerDao.getIdFromGroup() }
    }


    // Area
    internal fun getArea(uId: Long): CompletableFuture<Area> {
        return CompletableFuture.supplyAsync { areaDao.get(uId) }
    }

    internal fun getAreaWithTitle(title: String): CompletableFuture<Area> {
        return CompletableFuture.supplyAsync { areaDao.findAreaByTitle(title) }
    }

    fun allAreas(): CompletableFuture<List<Area>> {
        return CompletableFuture.supplyAsync { areaDao.getAll() }
    }

    fun getAreasForMarker(markerId: Long, group: Array<Int> = GROUPS_ALL): CompletableFuture<List<Area>> {
        return when (markerId) {
            NEW_CURRENT_MARKER -> CompletableFuture.supplyAsync { areaDao.getAll() }
            else -> CompletableFuture.supplyAsync { markerAreaDao.getAreasForMarker(markerId, group) }
        }
    }

    // AreaVisual
    fun getAreaVisual(areaId: Long): CompletableFuture<AreaVisual> {
        return CompletableFuture.supplyAsync {
            val area = areaDao.get(areaId)
            setupAreaVisual(area)
        }
    }

    fun getAreaVisualsForMarker(markerId: Long, groups: Array<Int>): CompletableFuture<ArrayList<AreaVisual>> {
        val areaVisuals = ArrayList<AreaVisual>()

        return CompletableFuture.supplyAsync {
            val areas = markerAreaDao.getAreasForMarker(markerId, groups)
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

    private fun handleException(throwable: Throwable) {
        Log.e(AppRepository::class.simpleName, "Database error", throwable)
    }
}
