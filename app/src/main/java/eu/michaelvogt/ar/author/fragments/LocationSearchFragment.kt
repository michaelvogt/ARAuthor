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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.tuples.SearchLocation
import eu.michaelvogt.ar.author.databinding.FragmentLocationSearchBinding
import eu.michaelvogt.ar.author.fragments.adapters.LocationSearchAdapter
import eu.michaelvogt.ar.author.utils.CardLinkListener
import eu.michaelvogt.ar.author.utils.InfoPrompt
import kotlinx.android.synthetic.main.fragment_location_search.*
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list

class LocationSearchFragment : AppFragment(), CardLinkListener {
    private lateinit var adapter: LocationSearchAdapter
    private lateinit var binder: FragmentLocationSearchBinding

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = FragmentLocationSearchBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binder.locationList.setHasFixedSize(true)

        adapter = LocationSearchAdapter(context, this)
        binder.locationList.adapter = adapter

        val appBarConfiguration = AppBarConfiguration.Builder(R.id.location_list_fragment).build()
        NavigationUI.setupWithNavController(top_toolbar, navController, appBarConfiguration)

        setupToolbar(R.menu.toolbar_locationsearch_menu, Toolbar.OnMenuItemClickListener {
            InfoPrompt.showOverlayInfo(this, R.id.toolbar_location_info,
                    R.string.location_search_primary, R.string.location_search_secondary)
        })

        viewModel.requestAvailableLocations(context!!,
                StringRequest(Request.Method.GET, resources.getString(R.string.available_locations),
                        Response.Listener {
                            val locations = JSON.parse(SearchLocation.serializer().list, it)
                            adapter.setLocations(locations)
                        },
                        Response.ErrorListener { println(it) }))
    }

    override fun onResume() {
        super.onResume()

        setupFab(android.R.drawable.ic_input_add, View.OnClickListener {

        })

        showBottomBar()
    }

    override fun onTextClicked(moduleId: String) {
        // TODO: Start download of selected module from Play store

        navController.popBackStack()
    }
}
