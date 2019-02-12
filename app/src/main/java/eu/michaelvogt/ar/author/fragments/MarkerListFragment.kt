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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.databinding.FragmentMarkerlistBinding
import eu.michaelvogt.ar.author.fragments.adapters.MarkerListAdapter
import eu.michaelvogt.ar.author.utils.InfoPrompt
import eu.michaelvogt.ar.author.utils.ItemClickListener
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_MARKER
import eu.michaelvogt.ar.author.utils.notDoneYet
import kotlinx.android.synthetic.main.fragment_markerlist.*

/**
 * View to list the [Marker]s of a [Location].
 *
 * A marker is currently necessary as a way ti find out where the camera is currently located at.
 * As soon as the ARCloud is available, they should be replaced by its localisations feature.
 *
 * A tab on a list item opens the pages to edit the data of that marker. A new marker can be
 * created through a FAB.
 */
class MarkerListFragment : AppFragment(), ItemClickListener {
    private lateinit var binder: FragmentMarkerlistBinding
    private lateinit var adapter: MarkerListAdapter

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = FragmentMarkerlistBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binder.markerList.setHasFixedSize(false)

        NavigationUI.setupWithNavController(top_toolbar, navController)

        setupToolbar(R.menu.toolbar_markerlist_menu, Toolbar.OnMenuItemClickListener {
            InfoPrompt.showOverlayInfo(this, R.id.toolbar_marker_info,
                    R.string.marker_info_primary, R.string.marker_info_secondary)
        })

        adapter = MarkerListAdapter(context)
        binder.markerList.adapter = adapter

        val markers = viewModel.getMarkerGroupsForLocation(context, viewModel.currentLocationId)
        adapter.setMarkers(markers)
        adapter.setItemClickListener(this)
    }

    override
    fun onResume() {
        super.onResume()

        setupBottomNav(R.menu.actionbar_markerlist_menu, Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.actionbar_markerlist_load -> {
                    notDoneYet(activity!!, "Load Markers")
                    true
                }
                R.id.actionbar_markerlist_ar -> {
                    notDoneYet(activity!!, "Preview Markers")
                    true
                }
                else -> false
            }
        })

        setupFab(android.R.drawable.ic_input_add, View.OnClickListener {
            viewModel.currentMarkerId = NEW_CURRENT_MARKER
            navController.navigate(MarkerListFragmentDirections.actionEditMarker())
        })

        viewModel.updateMarkerCache(activity!!, viewModel.currentLocationId)
    }

    override
    fun onItemClicked(uId: Long) {
        viewModel.currentMarkerId = uId
        navController.navigate(MarkerListFragmentDirections.actionEditMarker())
    }

    companion object {
        private val TAG = MarkerListFragment::class.java.simpleName
    }
}