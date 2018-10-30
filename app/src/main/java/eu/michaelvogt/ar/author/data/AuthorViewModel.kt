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
import android.arch.lifecycle.AndroidViewModel
import java.util.concurrent.CompletableFuture

class AuthorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(AppDatabase.getDatabase(application.applicationContext))

    var currentLocationId: Long = -1

    var currentMarkerId: Long = -1

    var currentAreaId: Long = -1

    var markersCache: List<Marker>? = null

    var cropMarker: Marker? = null

    // TODO: Set to current system language or english when not available
    var currentLanguage = "_jp_"

    // TODO: Area title for now. Should be the Area row id
    var currentMainContentId = "Muneoka Background Intro"


    fun addLocation(location: Location) = repository.insert(location)

    fun getLocation(uid: Long): CompletableFuture<Location> = repository.getLocation(uid)

    fun getAllLocations(): CompletableFuture<List<Location>> = repository.allLocations()


    fun addMarker(marker: Marker) = repository.insert(marker)

    fun getMarker(uId: Long): CompletableFuture<Marker> = repository.getMarker(uId)

    fun updateMarker(marker: Marker) = repository.update(marker)

    fun getMarkersForLocation(locationId: Long, withTitles: Boolean):
            CompletableFuture<List<Marker>> = repository.getMarkersForLocation(locationId, withTitles)

    fun clearCropMarker() {
        cropMarker = null
    }

    fun getAreasForMarker(markerId: Long, group: Int = GROUP_ALL):
            CompletableFuture<List<Area>> = repository.getAreasForMarker(markerId, group)


    fun getAreaVisual(uId: Long):
            CompletableFuture<AreaVisual> = repository.getAreaVisual(uId)

    fun getAreaVisualsForMarker(uId: Long, group: Int = GROUP_ALL):
            CompletableFuture<ArrayList<AreaVisual>> = repository.getAreaVisualsForMarker(uId, group)

    fun updateArea(area: Area) = repository.update(area)
}
