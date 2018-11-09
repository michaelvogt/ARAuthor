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
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.data.NEW_CURRENT_AREA
import eu.michaelvogt.ar.author.data.NEW_CURRENT_MARKER
import eu.michaelvogt.ar.author.databinding.FragmentLocationlistBinding
import eu.michaelvogt.ar.author.fragments.adapters.LocationListAdapter
import eu.michaelvogt.ar.author.utils.CardMenuHandler
import eu.michaelvogt.ar.author.utils.ItemClickListener

class LocationlistFragment : Fragment(), ItemClickListener {
    private lateinit var viewModel: AuthorViewModel
    private lateinit var adapter: LocationListAdapter

    private lateinit var binder: FragmentLocationlistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binder = FragmentLocationlistBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        binder.locationList.setHasFixedSize(true)

        adapter = LocationListAdapter(context, HandleLocationMenu())
        binder.locationList.adapter = adapter
        adapter.setItemClickListener(this)

        setLocations()
    }

    override
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.actionbar_locationlist_menu, menu)
    }

    override
    fun onItemClicked(uId: Long) {
        viewModel.currentLocationId = uId
        viewModel.currentMarkerId = NEW_CURRENT_MARKER
        viewModel.currentAreaId = NEW_CURRENT_AREA

        Navigation.findNavController(view!!).navigate(R.id.action_location_intro)
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
            val popupMenu = PopupMenu(context, view)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.menu_location_popup, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_location_delete -> {
                        viewModel.deleteLocation(location)!!
                                .thenAccept { activity!!.runOnUiThread { this@LocationlistFragment.setLocations() } }
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