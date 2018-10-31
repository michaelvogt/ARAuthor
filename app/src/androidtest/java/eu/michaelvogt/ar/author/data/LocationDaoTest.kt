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

import androidx.test.runner.AndroidJUnit4
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
        val all = dao!!.getAll()

        assertThat(all, equalTo(emptyList()))
    }

    @Test
    fun getSize() {
        dao!!.insertAll(*TestUtil.locations())
        val size = dao!!.getSize()

        assertThat(size, equalTo(TestUtil.locations().size))
    }

    @Test
    fun getByName() {
        val location1 = TestUtil.location1()

        dao!!.insert(location1)
        val byName = dao!!.findLocationByName(location1.name)

        location1.uId = byName.uId
        assertThat<Location>(byName, equalTo(location1))
    }
}