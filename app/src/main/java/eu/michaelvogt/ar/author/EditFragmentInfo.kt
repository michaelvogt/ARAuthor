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

package eu.michaelvogt.ar.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import eu.michaelvogt.ar.author.data.Marker

class EditFragmentInfo : Fragment(), PopupMenu.OnMenuItemClickListener {
    private lateinit var marker: Marker

    private lateinit var popupButton: Button
    private lateinit var introText: TextView

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_editmarker_info, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        introText = view.findViewById(R.id.edit_intro)
        introText.text = marker.intro
        introText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                marker.intro = introText.text.toString()
            }
        }

        popupButton = view.findViewById(R.id.edit_type)
        popupButton.setOnClickListener { popupView ->
            val popup = PopupMenu(context!!, popupView)
            popup.inflate(R.menu.marker_object_types)
            popup.setOnMenuItemClickListener(this@EditFragmentInfo)
            popup.show()
        }
    }

    override
    fun onMenuItemClick(item: MenuItem): Boolean {
        popupButton.text = item.title

        return when (item.itemId) {
            R.id.marker_type_building -> true
            R.id.marker_type_location -> true
            R.id.marker_type_infoboard -> true
            else -> false
        }
    }

    private fun setMarker(marker: Marker) {
        this.marker = marker
    }

    companion object {

        fun instantiate(marker: Marker): Fragment {
            val tabFragment = EditFragmentInfo()
            tabFragment.setMarker(marker)
            return tabFragment
        }
    }
}