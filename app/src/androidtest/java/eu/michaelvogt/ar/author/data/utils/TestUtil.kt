package eu.michaelvogt.ar.author.data.utils

import eu.michaelvogt.ar.author.data.*

object TestUtil {
    fun location1(name: String = "home"): Location {
        return Location(name, "thumb", "intro")
    }
    fun location2(name: String = "office"): Location {
        return Location(name, "thumb", "intro")
    }
    fun locations(name1: String = "home", name2: String = "office"): Array<Location> {
        return arrayOf(location1(name1), location2(name2))
    }

    fun markerTitle(locationId: Long, title: String = "inside"): Marker {
        return Marker(locationId, title, isTitle = true)
    }
    fun marker(locationId: Long, title: String = "board"): Marker {
        return Marker(locationId, title, isTitle = false)
    }
    fun markers(locationId: Long, title1: String = "inside", title2: String = "board"): Array<Marker> {
        return arrayOf(markerTitle(locationId, title1), marker(locationId, title2))
    }

    fun area1(title: String = "photo"): Area {
        return Area(title, TYPE_3DOBJECTONIMAGE, KIND_UI)
    }
    fun area2(title: String = "text"): Area {
        return Area(title, TYPE_DEFAULT, KIND_CONTENT)
    }
    fun areas(title1: String = "photo", title2: String = "text"): Array<Area> {
        return arrayOf(area1(title1), area2(title2))
    }

    fun markerArea(markerId: Long, areaId: Long): MarkerArea {
        return MarkerArea(markerId, areaId)
    }
}
