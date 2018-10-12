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
import android.support.test.runner.AndroidJUnit4
import eu.michaelvogt.ar.author.data.utils.LiveDataTestUtil
import eu.michaelvogt.ar.author.data.utils.TestUtil
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkerAreaDaoTest : DaoTest() {
    private var locationDao: LocationDao? = null
    private var markerDao: MarkerDao? = null
    private var areaDao: AreaDao? = null
    private var markerAreaDao: MarkerAreaDao? = null

    @Before
    fun setUp() {
        locationDao = db!!.locationDao()
        markerDao = db!!.markerDao()
        areaDao = db!!.areaDao()
        markerAreaDao = db!!.markerAreaDao()
    }


    @Test
    fun getAllFromEmptyTable() {
        val all = LiveDataTestUtil.getValue(markerAreaDao!!.getAll())

        Assert.assertThat(all, IsEqual.equalTo(emptyList()))
    }

    @Test
    fun getSize() {
        val locationId = locationDao!!.insert(TestUtil.location1())
        val markerId = markerDao!!.insert(TestUtil.marker(locationId))
        val areaId = areaDao!!.insert(TestUtil.area1())

        markerAreaDao!!.insert(TestUtil.markerArea(markerId, areaId))

        val size = LiveDataTestUtil.getValue(markerAreaDao!!.getSize())

        Assert.assertThat(size, IsEqual.equalTo(1))
    }

    @Test
    fun cantInsertMarkerAreaWithoutMarkerAndArea() {
        try {
            markerAreaDao!!.insert(TestUtil.markerArea(0, 0))
            Assert.fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun cantInsertMarkerAreaWithoutArea() {
        val locationId = locationDao!!.insert(TestUtil.location1())
        val markerId = markerDao!!.insert(TestUtil.marker(locationId))

        try {
            markerAreaDao!!.insert(TestUtil.markerArea(markerId, 0))
            Assert.fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun cantInsertMarkerAreaWithoutMarker() {
        val areaId = areaDao!!.insert(TestUtil.area1())

        try {
            markerAreaDao!!.insert(TestUtil.markerArea(0, areaId))
            Assert.fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun getMarkersForArea() {
        val locationId = locationDao!!.insert(TestUtil.location1())

        val markerId1 = markerDao!!.insert(TestUtil.marker(locationId, "entrance"))
        val markerId2 = markerDao!!.insert(TestUtil.marker(locationId, "exit"))

        val areaId1 = areaDao!!.insert(TestUtil.area1("floor plan"))
        val areaId2 = areaDao!!.insert(TestUtil.area1("explanation"))
        val areaId3 = areaDao!!.insert(TestUtil.area1("map"))

        markerAreaDao!!.insert(TestUtil.markerArea(markerId1, areaId1))
        markerAreaDao!!.insert(TestUtil.markerArea(markerId1, areaId2))

        markerAreaDao!!.insert(TestUtil.markerArea(markerId1, areaId3))
        markerAreaDao!!.insert(TestUtil.markerArea(markerId2, areaId3))

        val marker1Areas = markerAreaDao!!.getAreasForMarker(markerId1)
        val marker2Areas = markerAreaDao!!.getAreasForMarker(markerId2)

        val area1Markers = markerAreaDao!!.getMarkersForArea(areaId1)
        val area3Markers = markerAreaDao!!.getMarkersForArea(areaId3)

        Assert.assertThat(marker1Areas.size, IsEqual.equalTo(3))
        Assert.assertThat(marker2Areas.size, IsEqual.equalTo(1))
        Assert.assertThat(marker2Areas.get(0).title, IsEqual.equalTo("map"))

        Assert.assertThat(area1Markers.size, IsEqual.equalTo(1))
        Assert.assertThat(area3Markers.size, IsEqual.equalTo(2))
        Assert.assertThat(area1Markers.get(0).title, IsEqual.equalTo("entrance"))
    }

    @Test
    fun getAreasForMarker() {

    }

}
