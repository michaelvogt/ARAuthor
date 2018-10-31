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

import android.database.sqlite.SQLiteConstraintException
import androidx.test.runner.AndroidJUnit4
import eu.michaelvogt.ar.author.data.utils.TestUtil
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AnyOf.anyOf
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkerAreaDaoTest : DaoTest() {
    private lateinit var locationDao: LocationDao
    private lateinit var markerDao: MarkerDao
    private lateinit var areaDao: AreaDao
    private lateinit var markerAreaDao: MarkerAreaDao

    @Before
    fun setUp() {
        locationDao = db!!.locationDao()
        markerDao = db!!.markerDao()
        areaDao = db!!.areaDao()
        markerAreaDao = db!!.markerAreaDao()
    }


    @Test
    fun getAllFromEmptyTable() {
        val all = markerAreaDao.getAll()

        assertThat(all, equalTo(emptyList()))
    }

    @Test
    fun getSize() {
        val locationId = locationDao.insert(TestUtil.location1())
        val markerId = markerDao.insert(TestUtil.marker(locationId))
        val areaId = areaDao.insert(TestUtil.area1())

        markerAreaDao.insert(TestUtil.markerArea(markerId, areaId))

        val size = markerAreaDao.getSize()

        assertThat(size, equalTo(1))
    }

    @Test
    fun cantInsertMarkerAreaWithoutMarkerAndArea() {
        try {
            markerAreaDao.insert(TestUtil.markerArea(0, 0))
            fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun cantInsertMarkerAreaWithoutArea() {
        val locationId = locationDao.insert(TestUtil.location1())
        val markerId = markerDao.insert(TestUtil.marker(locationId))

        try {
            markerAreaDao.insert(TestUtil.markerArea(markerId, 0))
            fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun cantInsertMarkerAreaWithoutMarker() {
        val areaId = areaDao.insert(TestUtil.area1())

        try {
            markerAreaDao.insert(TestUtil.markerArea(0, areaId))
            fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun getMarkersForArea() {
        val (markerId1, markerId2) = insertSampleItems()

        val marker1Areas = markerAreaDao.getAreasForMarker(markerId1)
        val marker2Areas = markerAreaDao.getAreasForMarker(markerId2)

        assertThat(marker1Areas.size, equalTo(3))
        assertThat(marker2Areas.size, equalTo(1))
        assertThat(marker2Areas[0].title, equalTo("map"))
    }

    @Test
    fun getAreasForMarker() {
        val (markerId1, markerId2, areaId1, areaId2, areaId3) = insertSampleItems()

        val area1Markers = markerAreaDao.getMarkersForArea(areaId1)
        val area3Markers = markerAreaDao.getMarkersForArea(areaId3)

        assertThat(area1Markers.size, equalTo(1))
        assertThat(area3Markers.size, equalTo(2))
        assertThat(area1Markers[0].title, equalTo("entrance"))
    }

    @Test
    fun getAreaGroupForMarker() {
        val (markerId1) = insertSampleItems()

        val areaGroupMarkers = markerAreaDao.getAreaGroupForMarker(markerId1, GROUP_START)

        assertThat(areaGroupMarkers.size, equalTo(2))
        assertThat(areaGroupMarkers[1].title, anyOf(equalTo("explanation"), equalTo("map")))
    }


    private fun insertSampleItems(): List<Long> {
        val locationId = locationDao.insert(TestUtil.location1())

        val markerId1 = markerDao.insert(TestUtil.marker(locationId, "entrance"))
        val markerId2 = markerDao.insert(TestUtil.marker(locationId, "exit"))

        val areaId1 = areaDao.insert(TestUtil.area1("floor plan"))
        val areaId2 = areaDao.insert(TestUtil.area1("explanation", group = GROUP_START))
        val areaId3 = areaDao.insert(TestUtil.area1("map", group = GROUP_ALL))

        markerAreaDao.insert(TestUtil.markerArea(markerId1, areaId1))
        markerAreaDao.insert(TestUtil.markerArea(markerId1, areaId2))

        markerAreaDao.insert(TestUtil.markerArea(markerId1, areaId3))
        markerAreaDao.insert(TestUtil.markerArea(markerId2, areaId3))

        return listOf(markerId1, markerId2, areaId1, areaId2, areaId3)
    }
}
