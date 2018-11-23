package eu.michaelvogt.ar.author.data.utils

import eu.michaelvogt.ar.author.data.*
import java.util.*

object TestUtil {
    fun location1(name: String = "home"): Location {
        return Location(name, "desc", "thumb", "intro")
    }

    fun location2(name: String = "office"): Location {
        return Location(name, "desc", "thumb", "intro")
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


    fun area1(title: String = "photo", group: Int = GROUP_NONE, usage: Int = KIND_CONTENT): Area {
        return Area(title, TYPE_3DOBJECTONIMAGE, usage, group = group)
    }

    fun area2(title: String = "text", group: Int = GROUP_NONE, usage: Int = KIND_CONTENT): Area {
        return Area(title, TYPE_DEFAULT, usage, group = group)
    }

    fun areas(title1: String = "photo", title2: String = "text"): Array<Area> {
        return arrayOf(area1(title1), area2(title2))
    }

    fun markerArea(markerId: Long, areaId: Long): MarkerArea {
        return MarkerArea(markerId, areaId)
    }


    fun visualDetail1(areaId: Long, type: Int = 0, key: Int = 0, value: Any = ""): VisualDetail {
        return VisualDetail(areaId, type, key, value)
    }

    fun visualDetail2(areaId: Long, type: Int = 0, key: Int = 0, value: Any = ""): VisualDetail {
        return VisualDetail(areaId, type, key, value)
    }

    fun visualDetails(areaId: Long): List<VisualDetail> {
        return listOf(visualDetail1(areaId), visualDetail2(areaId))
    }


    fun slide1(detailId: Long, type: Int = TYPE_IMAGE, order: Int = 0, imagePath: String = "slide1",
               secondaryPaths: List<String> = ArrayList(), description: String = "slide1 desc"): Slide {
        return Slide(detailId, type, order, imagePath, secondaryPaths, description)
    }

    fun slide2(detailId: Long, type: Int = TYPE_IMAGE, order: Int = 0, imagePath: String = "slide2",
               secondaryPaths: List<String> = ArrayList(), description: String = "slide2 desc"): Slide {
        return Slide(detailId, type, order, imagePath, secondaryPaths, description)
    }

    fun slides(detailId: Long): Array<Slide> {
        return arrayOf(slide1(detailId, order = 0), slide2(detailId, order = 1))
    }
}
