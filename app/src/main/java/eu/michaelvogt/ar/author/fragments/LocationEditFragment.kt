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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.databinding.FragmentLocationEditBinding
import kotlinx.android.synthetic.main.fragment_location_edit.*

/**
 * Fragment to edit a location
 */
class LocationEdit : Fragment() {
    private lateinit var binder: FragmentLocationEditBinding
    private lateinit var viewModel: AuthorViewModel

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = FragmentLocationEditBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationId = LocationEditArgs.fromBundle(arguments).locationId
        var location: Location = Location("New Location", "", "")
        val navController = Navigation.findNavController(view)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        viewModel.getLocation(locationId).thenAccept {
            location = it
            binder.location = location
        }

        location_edit_save_button.setOnClickListener {
            viewModel.updateLocation(location)
            navController.popBackStack()
        }

        location_edit_cancel_button.setOnClickListener {
            navController.popBackStack()
        }
    }
}
