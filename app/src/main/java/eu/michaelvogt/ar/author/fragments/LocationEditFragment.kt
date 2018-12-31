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

package eu.michaelvogt.ar.author.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.NavigationUI
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.databinding.FragmentLocationEditBinding
import eu.michaelvogt.ar.author.utils.FileUtils
import eu.michaelvogt.ar.author.utils.PATH_SELECT_LOCATION_INTRO_CODE
import eu.michaelvogt.ar.author.utils.PATH_SELECT_THUMB_CODE
import kotlinx.android.synthetic.main.fragment_location_edit.*

/**
 * Fragment to edit a [Location]
 */
class LocationEditFragment : AppFragment() {
    private lateinit var binder: FragmentLocationEditBinding
    private lateinit var fabListener: View.OnClickListener

    private var locationId: Long = -1L
    private var location: Location = Location("New Location", "", "", "", "")

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = FragmentLocationEditBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationId = viewModel.currentLocationId

        if (locationId == -1L) {
            binder.location = location
            fabListener = View.OnClickListener {
                viewModel.insertLocation(location).thenAccept {
                    activity!!.runOnUiThread { navController.popBackStack() }
                }.exceptionally {
                    Log.e(TAG, "Unable to insert new location $locationId")
                    null
                }
            }
        } else {
            viewModel.getLocation(locationId).thenAccept {
                location = it
                binder.location = it
            }

            fabListener = View.OnClickListener {
                viewModel.updateLocation(location).thenAccept {
                    activity!!.runOnUiThread { navController.popBackStack() }
                }.exceptionally {
                    Log.e(TAG, "Unable to update location $locationId")
                    null
                }
            }
        }

        NavigationUI.setupWithNavController(top_toolbar, navController)

        binder.locationEditThumbSelect
                .setOnClickListener { handleImageSelection("image/*", PATH_SELECT_THUMB_CODE) }
        binder.locationEditIntroSelect
                .setOnClickListener { handleImageSelection("text/html", PATH_SELECT_LOCATION_INTRO_CODE) }
    }

    override fun onResume() {
        super.onResume()

        setupFab(android.R.drawable.ic_menu_save, fabListener)
        setupBottomNav(R.menu.actionbar_locationedit_menu, Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.actionbar_location_delete -> {
                    viewModel.deleteLocation(location).thenAccept {
                        activity!!.runOnUiThread { navController.popBackStack() }
                    }
                    true
                }
                else -> false
            }
        })
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

    companion object {
        private val TAG = LocationEditFragment::class.java.simpleName
    }
}
