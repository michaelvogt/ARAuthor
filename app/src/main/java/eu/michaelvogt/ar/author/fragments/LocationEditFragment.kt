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

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.databinding.FragmentLocationEditBinding
import eu.michaelvogt.ar.author.utils.FileUtils
import eu.michaelvogt.ar.author.utils.PATH_SELECT_LOCATION_INTRO_CODE
import eu.michaelvogt.ar.author.utils.PATH_SELECT_THUMB_CODE
import kotlinx.android.synthetic.main.fragment_location_edit.*

/**
 * Fragment to edit a location
 */
class LocationEdit : Fragment() {
    private lateinit var binder: FragmentLocationEditBinding
    private lateinit var viewModel: AuthorViewModel
    private lateinit var navController: NavController

    private var locationId: Long = -1L
    private var location: Location = Location("New Location", "", "", "")

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binder = FragmentLocationEditBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationId = LocationEditArgs.fromBundle(arguments).locationId

        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        if (locationId != -1L) {
            viewModel.getLocation(locationId).thenAccept {
                location = it
                binder.location = location
            }

            location_edit_save_button.setOnClickListener {
                viewModel.updateLocation(location)
                navController.popBackStack()
            }
        } else {
            binder.location = location
            location_edit_save_button.setOnClickListener {
                viewModel.insertLocation(location)
                navController.popBackStack()
            }
        }

        location_edit_cancel_button.setOnClickListener {
            navController.popBackStack()
        }

        binder.locationEditThumbSelect.setOnClickListener { handleImageSelection("image/*", PATH_SELECT_THUMB_CODE) }
        binder.locationEditIntroSelect.setOnClickListener { handleImageSelection("text/html", PATH_SELECT_LOCATION_INTRO_CODE) }
    }

    override
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (locationId != -1L) {
            inflater!!.inflate(R.menu.actionbar_locationedit_menu, menu)
        }
    }

    override
    fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.actionbar_location_delete -> {
                viewModel
                        .deleteLocation(location)!!
                        .thenAccept { activity!!.runOnUiThread { navController.popBackStack() } }
            }
        }

        return false
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val path = FileUtils.getRelativePath(data?.data?.path)

        when (requestCode) {
            PATH_SELECT_THUMB_CODE ->
                setRelativePath(binder.locationEditThumb.editText?.text, path)

            PATH_SELECT_LOCATION_INTRO_CODE ->
                setRelativePath(binder.locationEditIntro.editText?.text, path)
        }
    }

    private fun setRelativePath(editable: Editable?, path: String) {
        editable?.clear()
        editable?.append(path)
    }

    private fun handleImageSelection(mimeType: String, code: Int) {
        val intent = Intent()
        intent.type = mimeType
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(intent, code)
    }
}
