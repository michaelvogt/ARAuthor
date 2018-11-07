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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.GROUPS_ALL
import eu.michaelvogt.ar.author.data.NEW_CURRENT_MARKER
import eu.michaelvogt.ar.author.utils.AreaListAdapter
import eu.michaelvogt.ar.author.utils.ItemClickListener

class EditFragmentAreas : Fragment(), ItemClickListener {

    private var markerId = NEW_CURRENT_MARKER
    private lateinit var viewModel: AuthorViewModel

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_editmarker_areas, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.areas_list)
        recyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager

        val adapter = AreaListAdapter()
        adapter.setItemClickListener(this)

        recyclerView.adapter = adapter

        viewModel.getAreasForMarker(markerId, GROUPS_ALL)
                .thenAccept { areas -> activity!!.runOnUiThread { adapter.setAreas(areas) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch areas for marker $markerId", throwable)
                    null
                }
    }

    override
    fun onItemClicked(uId: Long) {
        viewModel.currentAreaId = uId
        Navigation.findNavController(view!!).navigate(R.id.action_edit_area_placement)
    }

    private fun setMarkerId(markerId: Long) {
        this.markerId = markerId
    }

    companion object {
        private val TAG = EditFragmentAreas::class.java.simpleName

        fun instantiate(markerId: Long): Fragment {
            val tabFragment = EditFragmentAreas()
            tabFragment.setMarkerId(markerId)
            return tabFragment
        }
    }
}