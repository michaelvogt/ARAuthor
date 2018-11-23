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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.databinding.FragmentLocationlistBinding
import eu.michaelvogt.ar.author.fragments.adapters.LocationListAdapter
import eu.michaelvogt.ar.author.utils.*
import kotlinx.android.synthetic.main.fragment_locationlist.*

class LocationlistFragment : Fragment(), ItemClickListener {
    private lateinit var viewModel: AuthorViewModel
    private lateinit var adapter: LocationListAdapter
    private lateinit var binder: FragmentLocationlistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = FragmentLocationlistBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        binder.locationList.setHasFixedSize(true)

        adapter = LocationListAdapter(context, HandleLocationMenu())
        adapter.setItemClickListener(this)
        binder.locationList.adapter = adapter

        info_button.setOnClickListener { showLocationInfo(this) }

        viewModel.locationLoadTrigger.observe(this, Observer { setLocations() })
    }

    override
    fun onItemClicked(uId: Long) {
        viewModel.currentLocationId = uId
        viewModel.currentMarkerId = NEW_CURRENT_MARKER
        viewModel.currentAreaId = NEW_CURRENT_AREA

        val bundle = Bundle()
        bundle.putInt("content_url", R.string.location_intro_key)
        Navigation.findNavController(view!!).navigate(R.id.web_view_fragment, bundle)
    }

    private fun setLocations() {
        viewModel.getAllLocations()
                .thenAccept { locations -> activity!!.runOnUiThread { adapter.setLocations(locations) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch all locations.", throwable)
                    null
                }
    }

    private inner class HandleLocationMenu : CardMenuHandler {
        override fun onMenuClick(view: View, location: Location) {
            val popupMenu = PopupMenu(context!!, view)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.menu_location_popup, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_location_edit -> {
                        val locationId = LocationlistFragmentDirections.actionToLocationEdit().setLocationId(location.uId).arguments
                        Navigation.findNavController(view).navigate(LocationlistFragmentDirections.actionToLocationEdit().actionId, locationId)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    companion object {
        private val TAG = LocationlistFragment::class.java.simpleName
    }
}