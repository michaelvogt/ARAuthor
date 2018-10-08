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

import android.support.test.runner.AndroidJUnit4
import eu.michaelvogt.ar.author.data.utils.LiveDataTestUtil
import eu.michaelvogt.ar.author.data.utils.TestUtil
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationDaoTest : DaoTest() {
    private var dao: LocationDao? = null

    @Before
    fun setUp() {
        dao = db!!.locationDao()
    }

    @Test
    fun getAllFromEmptyTable() {
        val all = LiveDataTestUtil.getValue(dao!!.getAll())

        assertThat(all, equalTo(emptyList()))
    }

    @Test
    fun getSize() {
        dao!!.insertAll(*TestUtil.locations())
        val size = LiveDataTestUtil.getValue(dao!!.getSize())

        assertThat(size, equalTo(TestUtil.locations().size))
    }

    @Test
    @Throws(Exception::class)
    fun getByName() {
        dao!!.insert(TestUtil.location1())
        val byName = LiveDataTestUtil.getValue(dao!!.findLocationByName(TestUtil.location1().name))

        assertThat<Location>(byName, equalTo(TestUtil.location1()))
    }
}