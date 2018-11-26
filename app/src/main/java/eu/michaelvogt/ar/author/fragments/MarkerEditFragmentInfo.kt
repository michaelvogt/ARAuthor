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

package eu.michaelvogt.ar.author.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Marker
import eu.michaelvogt.ar.author.databinding.FragmentMarkerEditInfoBinding

/**
 * Fragment to edit data related to the object represented by this [Marker]
 */
class MarkerEditFragmentInfo : AppFragment() {
    private lateinit var marker: Marker

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        val binder = FragmentMarkerEditInfoBinding.inflate(inflater, container, false)
        binder.handler = this
        binder.marker = marker
        return binder.root
    }

    fun onShowTypePopup(view: View) {
        val popup = PopupMenu(context!!, view)
        popup.inflate(R.menu.marker_object_types)
        popup.setOnMenuItemClickListener {
            val button = view as Button
            button.text = it.title

            when (it.itemId) {
                R.id.marker_type_building -> true
                R.id.marker_type_location -> true
                R.id.marker_type_infoboard -> true
                else -> false
            }
        }
        popup.show()
    }

    private fun setMarker(marker: Marker) {
        this.marker = marker
    }

    companion object {

        fun instantiate(marker: Marker): Fragment {
            val tabFragment = MarkerEditFragmentInfo()
            tabFragment.setMarker(marker)
            return tabFragment
        }
    }
}