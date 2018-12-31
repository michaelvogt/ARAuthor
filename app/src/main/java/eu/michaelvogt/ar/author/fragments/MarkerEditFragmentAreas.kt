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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import eu.michaelvogt.ar.author.data.GROUPS_ALL
import eu.michaelvogt.ar.author.databinding.FragmentMarkerEditAreasBinding
import eu.michaelvogt.ar.author.fragments.adapters.AreaListAdapter
import eu.michaelvogt.ar.author.utils.ItemClickListener
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_AREA
import eu.michaelvogt.ar.author.utils.NEW_CURRENT_MARKER
import kotlinx.android.synthetic.main.fragment_marker_edit_areas.*

/**
 * Fragment with the list of the [Area]s related to a [Marker]
 */
class MarkerEditFragmentAreas : AppFragment(), ItemClickListener {

    private var markerId = NEW_CURRENT_MARKER

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binder = FragmentMarkerEditAreasBinding.inflate(inflater, container, false)
        binder.handler = this
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mLayoutManager = LinearLayoutManager(context)
        areas_list.layoutManager = mLayoutManager
        areas_list.setHasFixedSize(true)

        val adapter = AreaListAdapter(context)
        adapter.setItemClickListener(this)
        areas_list.adapter = adapter

        marker_edit_area_chip_add.setOnClickListener {
            navigateToEdit(NEW_CURRENT_AREA)
        }

        viewModel.getAreasForMarker(markerId, GROUPS_ALL)
                .thenAccept { activity!!.runOnUiThread { adapter.setAreas(it) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch areas for marker $markerId", throwable)
                    null
                }
    }

    // Called from adapter
    override
    fun onItemClicked(uId: Long) {
        navigateToEdit(uId)
    }

    private fun navigateToEdit(areaId: Long) {
        viewModel.currentAreaId = areaId
        navController.navigate(MarkerEditFragmentDirections.actionToAreaEdit())
    }

    private fun setMarkerId(markerId: Long) {
        this.markerId = markerId
    }

    companion object {
        private val TAG = MarkerEditFragmentAreas::class.java.simpleName

        fun instantiate(markerId: Long): Fragment {
            val tabFragment = MarkerEditFragmentAreas()
            tabFragment.setMarkerId(markerId)
            return tabFragment
        }
    }
}