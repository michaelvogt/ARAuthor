/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AreaVisualTest : DaoTest() {
    private var areaDao: AreaDao? = null
    private var detailDao: VisualDetailDao? = null
    private var eventDao: EventDetailDao? = null

    @Before
    fun setUp() {
        areaDao = db.areaDao()
        detailDao = db.visualDetailDao()
        eventDao = db.eventDetailDao()
    }

    @Test
    fun getDetails() {
    }

    @Test
    fun getEvents() {

    }

    @Test
    fun hasDetail() {

    }

    @Test
    fun getDetail() {

    }

    @Test
    fun hasEvent() {

    }

    @Test
    fun apply() {

    }

    @Test
    fun getDefaultArea() {

    }

    @Test
    fun getBackgroundArea() {

    }
}