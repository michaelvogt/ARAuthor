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
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.data.NEW_CURRENT_AREA
import eu.michaelvogt.ar.author.data.NEW_CURRENT_MARKER
import eu.michaelvogt.ar.author.utils.CardMenuHandler
import eu.michaelvogt.ar.author.utils.ItemClickListener
import eu.michaelvogt.ar.author.utils.LocationListAdapter

class LocationlistFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: AuthorViewModel
    private lateinit var adapter: LocationListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_locationlist, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.location_list)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        adapter = LocationListAdapter(context, HandleLocationMenu())
        adapter.setItemClickListener(this)
        recyclerView.adapter = adapter

        setLocations()
    }

    override
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.actionbar_location_menu, menu)
    }

    override
    fun onItemClicked(locationId: Long) {
        val bottomNav = activity!!.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.menu.findItem(R.id.marker_list_fragment).isEnabled = true

        viewModel.currentLocationId = locationId
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
                                .thenAccept { unit -> activity!!.runOnUiThread { this@LocationlistFragment.setLocations() } }
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