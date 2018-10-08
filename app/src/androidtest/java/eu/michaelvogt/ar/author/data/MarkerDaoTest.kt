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
class MarkerDaoTest : DaoTest() {
    private var dao: MarkerDao? = null
    private var locationDao: LocationDao? = null

    @Before
    fun setUp() {
        dao = db!!.markerDao()
        locationDao = db!!.locationDao()
    }


    @Test
    fun getAllFromEmptyTable() {
        val all = LiveDataTestUtil.getValue(dao!!.getAll())

        Assert.assertThat(all, IsEqual.equalTo(emptyList()))
    }


    @Test
    fun getSize() {
        val locationId = locationDao!!.insert(TestUtil.location1()).toInt()

        dao!!.insertAll(*TestUtil.markers(locationId))
        val size = LiveDataTestUtil.getValue(dao!!.getSize())

        Assert.assertThat(size, IsEqual.equalTo(TestUtil.locations().size))
    }

    @Test
    @Throws(InterruptedException::class)
    fun cantInsertCommentWithoutProduct() {
        try {
            dao!!.insertAll(*TestUtil.markers(0))
            Assert.fail("SQLiteConstraintException expected")
        } catch (ignored: SQLiteConstraintException) {
        }
    }

    @Test
    @Throws(Exception::class)
    fun getByName() {
        val locationId = locationDao!!.insert(TestUtil.location1()).toInt()

        dao!!.insert(TestUtil.marker(locationId))
        val byName = LiveDataTestUtil.getValue(dao!!.findMarkerByTitle(TestUtil.marker(locationId).title))

        Assert.assertThat<Marker>(byName, IsEqual.equalTo(TestUtil.marker(locationId)))
    }
}