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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.MARKERS_AND_TITLES
import eu.michaelvogt.ar.author.data.NEW_CURRENT_MARKER
import eu.michaelvogt.ar.author.databinding.FragmentMarkerlistBinding
import eu.michaelvogt.ar.author.fragments.adapters.MarkerListAdapter
import eu.michaelvogt.ar.author.utils.ItemClickListener

class MarkerListFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: AuthorViewModel
    private lateinit var binder: FragmentMarkerlistBinding

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binder = FragmentMarkerlistBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)
        val locationId = viewModel.currentLocationId

        binder.markerList.setHasFixedSize(false)

//        val layoutManager = LinearLayoutManager(context)
//        binder.markerList.layoutManager = layoutManager

        val adapter = MarkerListAdapter(context)
        binder.markerList.adapter = adapter

        viewModel.getMarkersForLocation(locationId, MARKERS_AND_TITLES)
                .thenAccept { markers -> activity!!.runOnUiThread { adapter.setMarkers(markers) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch markers for location $locationId", throwable)
                    null
                }

        adapter.setItemClickListener(this)

//        val bottomNav = activity!!.findViewById<View>(R.id.bottom_nav)
//        val item = (bottomNav as BottomNavigationView).menu.findItem(R.id.bottom_ar)
//        item.setOnMenuItemClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_test_markers)
//            true
//        }
    }

    override
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (activity == null) return
        inflater!!.inflate(R.menu.actionbar_markerlist_menu, menu)
    }

    override
    fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.actionbar_markerlist_load -> {
                Log.i(TAG, "Load markers")
                return true
            }
            R.id.actionbar_markerlist_new -> {
                viewModel.currentMarkerId = NEW_CURRENT_MARKER
                Navigation.findNavController(view!!).navigate(R.id.action_edit_marker)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override
    fun onItemClicked(uId: Long) {
        viewModel.currentMarkerId = uId
        Navigation.findNavController(view!!).navigate(R.id.action_edit_marker)
    }

    companion object {
        private val TAG = MarkerListFragment::class.java.simpleName
    }
}