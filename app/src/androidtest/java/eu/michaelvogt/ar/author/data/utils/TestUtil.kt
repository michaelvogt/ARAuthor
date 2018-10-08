package eu.michaelvogt.ar.author.data.utils

import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.data.Marker

object TestUtil {
    fun location1() = Location("home", "thumb", "intro")
    fun location2() = Location("office", "thumb", "intro")
    fun locations() = arrayOf(location1(), location2())

    fun markerTitle(locationId: Int) = Marker(locationId, "inside")
    fun marker(locationId: Int) = Marker(title="board", locationId = locationId)
    fun markers(locationId: Int) = arrayOf(markerTitle(locationId), marker(locationId))
}
