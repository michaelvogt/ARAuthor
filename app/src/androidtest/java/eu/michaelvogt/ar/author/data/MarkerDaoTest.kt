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
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.michaelvogt.ar.author.data.utils.TestUtil
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkerDaoTest : DaoTest() {
    private lateinit var  markerDao: MarkerDao
    private lateinit var locationDao: LocationDao

    @Before
    fun setUp() {
        markerDao = db!!.markerDao()
        locationDao = db!!.locationDao()
    }


    @Test
    fun getAllFromEmptyTable() {
        val all = markerDao.getAll()

        Assert.assertThat(all, IsEqual.equalTo(emptyList()))
    }

    @Test
    fun getSize() {
        val locationId = locationDao.insert(TestUtil.location1())

        markerDao.insertAll(*TestUtil.markers(locationId))
        val size = markerDao.getSize()

        Assert.assertThat(size, IsEqual.equalTo(TestUtil.locations().size))
    }

    @Test
    fun cantInsertMarkertWithoutLocation() {
        try {
            markerDao.insertAll(*TestUtil.markers(0))
            Assert.fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    fun getByName() {
        val locationId = locationDao.insert(TestUtil.location1())
        val marker = TestUtil.marker(locationId)

        markerDao.insert(marker)
        val byName = markerDao.findMarkerByTitle(marker.title)

        marker.uId = byName.uId

        Assert.assertThat<Marker>(byName, IsEqual.equalTo(marker))
    }

    @Test
    fun getWithOrWithoutTitles() {
        val locationId = locationDao.insert(TestUtil.location1())

        markerDao.insert(TestUtil.markerTitle(locationId))
        markerDao.insertAll(TestUtil.marker(locationId))

        val withTitle = markerDao.findMarkersForLocation(locationId, arrayOf(0, 1))
        val withoutTitle = markerDao.findMarkersForLocation(locationId, arrayOf(0))

        Assert.assertThat<Int>(withTitle.size, IsEqual.equalTo(2))
        Assert.assertThat<Int>(withoutTitle.size, IsEqual.equalTo(1))
    }
}