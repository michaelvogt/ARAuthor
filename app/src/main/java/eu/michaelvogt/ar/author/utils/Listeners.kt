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

package eu.michaelvogt.ar.author.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.data.tuples.SearchLocation

interface CardLinkListener {
    fun onTextClicked(searchLocation: SearchLocation)
}

interface CardEventListener {
    fun onMenuClick(view: View, location: Location)
    fun onItemClicked(uId: Long)
    fun onDownloadClicked(location: Location, download: View,
                          progress: ProgressBar, label: View, size: View, message: TextView)
}

interface ItemClickListener {
    fun onItemClicked(uId: Long)
}

interface MenuSelectedListener {
    fun onMenuSelected(id: Int)
}
