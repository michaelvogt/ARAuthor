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
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.databinding.FragmentAreaListBinding
import eu.michaelvogt.ar.author.fragments.adapters.AreaListAdapter
import eu.michaelvogt.ar.author.utils.InfoPrompt
import eu.michaelvogt.ar.author.utils.ItemClickListener
import eu.michaelvogt.ar.author.utils.notDoneYet
import kotlinx.android.synthetic.main.fragment_area_list.*

/**
 * View to list the [Area]s of a [Marker], or all Areas when no Marker is set
 *
 * Areas are content spaces placed on objects localised by a Marker. An Area can for example display
 * text, images or 3D image and offers respective interactive functions.
 */
class AreaListFragment : AppFragment(), ItemClickListener {
    private lateinit var adapter: AreaListAdapter
    private lateinit var binder: FragmentAreaListBinding

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = FragmentAreaListBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        area_list.layoutManager = layoutManager
        area_list.setHasFixedSize(true)

        adapter = AreaListAdapter(context)
        adapter.setItemClickListener(this)
        area_list.adapter = adapter

        viewModel.getAllAreas()
                .thenAccept { activity!!.runOnUiThread { adapter.setAreas(it) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch areas for marker", throwable)
                    null
                }

        NavigationUI.setupWithNavController(top_toolbar, navController)

        setupToolbar(R.menu.toolbar_arealist_menu, Toolbar.OnMenuItemClickListener {
            InfoPrompt.showOverlayInfo(this,
                    R.id.toolbar_area_info, R.string.area_info_primary, R.string.area_info_secondary)
        })
    }

    override
    fun onResume() {
        super.onResume()

        setupBottomNav(R.menu.actionbar_arealist_menu, Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.actionbar_arealist_load,
                R.id.actionbar_arealist_ar -> {
                    notDoneYet(activity!!, "AR Scene")
                    true
                }
                else -> false
            }
        })

        // Temporary hide Area creation, until input of Area details is decided
        hideFab()
//        setupFab(android.R.drawable.ic_input_add, View.OnClickListener {
//            viewModel.currentAreaId = NEW_CURRENT_AREA
//            navController.navigate(AreaListFragmentDirections.actionToAreaEdit())
//        })
    }

    override
    fun onItemClicked(uId: Long) {
        viewModel.currentAreaId = uId
        navController.navigate(AreaListFragmentDirections.actionToAreaEdit())
    }

    companion object {
        private val TAG = AreaListFragment::class.java.simpleName
    }
}
