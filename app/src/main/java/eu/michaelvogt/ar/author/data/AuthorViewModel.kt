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

import java.util.ArrayList
import java.util.Optional

class AuthorViewModel(application: Application) : AndroidViewModel(application) {
    private val markers = ArrayList<Marker>()
    private val areas = ArrayList<Area>()
    private val locations = ArrayList<Location>()

    var cropMarker: Marker? = null

    // TODO: Set to current system language or english when not available
    var currentLanguage = "_jp_"

    // TODO: Area title for now. Should be the Area object id
    var currentMainContentId = "Muneoka Background Intro"

    val markerSize: Int
        get() = markers.size

    fun addMarker(marker: Marker) {
        markers.add(marker)
    }

    fun getEditMarker(index: Int): Marker {
        return Marker(markers[index])
    }

    fun getMarker(index: Int): Marker {
        return markers[index]
    }

    fun getMarkerFromString(name: String): Optional<Marker> {
        return markers.stream().filter { marker -> marker.title == name }.findFirst()
    }

    fun getMarkerFromUid(id: Int): Optional<Marker> {
        return markers.stream().filter { marker -> marker.uid == id }.findFirst()
    }

    fun setMarker(index: Int, marker: Marker) {
        markers[index] = marker
    }

    fun markerIterable(): Iterable<Marker> {
        return markers
    }

    fun clearCropMarker() {
        cropMarker = null
    }


    val areaSize: Int
        get() = areas.size

    fun addArea(area: Area) {
        areas.add(area)
    }

    fun getArea(index: Int): Area {
        return areas[index]
    }

    fun getArea(areaId: String): Optional<Area> {
        return areas.stream().filter { area -> area.title == areaId }.findFirst()
    }

    fun addLocation(location: Location) {
        locations.add(location)
    }

    fun getLocation(index: Int): Location {
        return locations.get(index)
    }

    fun getLocationSize(): Int {
        return locations.size;
    }
}
