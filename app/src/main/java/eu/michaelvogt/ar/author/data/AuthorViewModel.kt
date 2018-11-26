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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_AREA
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_LOCATION
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_MARKER
import java.util.concurrent.CompletableFuture

class AuthorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(AppDatabase.getDatabase(application.applicationContext))

    var currentLocationId: Long = NEW_CURRENT_LOCATION

    var currentMarkerId: Long = NEW_CURRENT_MARKER

    var currentAreaId: Long = NEW_CURRENT_AREA

    var markersCache: List<Marker>? = null

    var cropMarker: Marker? = null

    // TODO: Set to current system language or english when not available
    var currentLanguage = "_jp_"

    // TODO: Area title for now. Should be the Area row id
    var currentMainContentId = "Muneoka Background Intro"

    var locationLoadTrigger: MutableLiveData<Int> = MutableLiveData()

    fun insertLocation(location: Location) = repository.insert(location)

    fun getLocation(uid: Long): CompletableFuture<Location> = repository.getLocation(uid)

    fun getAllLocations(): CompletableFuture<List<Location>> = repository.allLocations()

    fun updateLocation(location: Location) = repository.update(location)

    fun deleteLocation(location: Location): CompletableFuture<Unit>? = repository.delete(location)


    fun insertMarker(marker: Marker) = repository.insert(marker)

    fun getMarker(uId: Long): CompletableFuture<Marker> = repository.getMarker(uId)

    fun updateMarker(marker: Marker) = repository.update(marker)

    fun deleteMarker(marker: Marker): CompletableFuture<Unit>? = repository.delete(marker)

    fun getMarkersForLocation(locationId: Long, withTitles: Array<Int>):
            CompletableFuture<List<Marker>> = repository.getMarkersForLocation(locationId, withTitles)

    fun clearCropMarker() {
        cropMarker = null
    }

    fun getAreasForMarker(markerId: Long, group: Array<Int> = GROUPS_ALL):
            CompletableFuture<List<Area>> = repository.getAreasForMarker(markerId, group)


    fun getAreaVisual(uId: Long):
            CompletableFuture<AreaVisual> = repository.getAreaVisual(uId)

    fun getAreaVisualsForMarker(uId: Long, group: Array<Int> = GROUPS_ALL):
            CompletableFuture<ArrayList<AreaVisual>> = repository.getAreaVisualsForMarker(uId, group)

    fun updateArea(area: Area) = repository.update(area)
}
