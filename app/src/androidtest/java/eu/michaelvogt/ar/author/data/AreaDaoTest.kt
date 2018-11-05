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

import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.michaelvogt.ar.author.data.utils.TestUtil
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AreaDaoTest : DaoTest() {
    private lateinit var dao: AreaDao

    @Before
    fun setUp() {
        dao = db!!.areaDao()
    }

    @Test
    fun getAllFromEmptyTable() {
        val all = dao.getAll()

        Assert.assertThat(all, equalTo(emptyList()))
    }

    @Test
    fun getSize() {
        dao.insertAll(*TestUtil.areas())
        val size = dao.getSize()

        Assert.assertThat(size, equalTo(TestUtil.areas().size))
    }

    @Test
    fun getByName() {
        val area = TestUtil.area1()

        dao.insert(area)
        val byName = dao.findAreaByTitle(area.title)

        area.uId = byName.uId

        Assert.assertThat<Area>(byName, equalTo(area))
    }

}