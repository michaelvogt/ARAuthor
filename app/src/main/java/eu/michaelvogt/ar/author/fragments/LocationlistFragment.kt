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
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*
import eu.michaelvogt.ar.author.data.utils.Json
import eu.michaelvogt.ar.author.databinding.FragmentLocationlistBinding
import eu.michaelvogt.ar.author.fragments.adapters.LocationListAdapter
import eu.michaelvogt.ar.author.modules.ModuleLoader
import eu.michaelvogt.ar.author.modules.ModuleLoaderCallback
import eu.michaelvogt.ar.author.utils.*
import kotlinx.android.synthetic.main.fragment_locationlist.*

/**
 * View to list the [Location]s provided by the view model.
 *
 * Tab on a list item navigates to the web view, which loads the content of the URL stored in
 * [Location.introHtmlPath].
 */
class LocationlistFragment : AppFragment(), CardEventListener {
    private lateinit var adapter: LocationListAdapter
    private lateinit var binder: FragmentLocationlistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = FragmentLocationlistBinding.inflate(inflater, container, false)
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binder.locationList.setHasFixedSize(true)

        viewModel.locationLoadTrigger.observe(this, Observer { setLocations() })

        adapter = LocationListAdapter(context, this)
        binder.locationList.adapter = adapter
        binder.mylocation.handler = adapter

        val appBarConfiguration = AppBarConfiguration.Builder(R.id.location_list_fragment).build()
        NavigationUI.setupWithNavController(top_toolbar, navController, appBarConfiguration)

        setupToolbar(R.menu.toolbar_locationlist_menu, Toolbar.OnMenuItemClickListener {
            InfoPrompt.showOverlayInfo(this,
                    R.id.toolbar_location_info, R.string.location_info_primary, R.string.location_info_secondary)
        })
    }

    override fun onResume() {
        super.onResume()

        setupFab(R.drawable.ic_search_black_24dp, AppFragment.Companion.FabVisibility.ALWAYS, View.OnClickListener {
            viewModel.currentLocationId = NEW_CURRENT_LOCATION
            navController.navigate(LocationlistFragmentDirections.actionToLocationSearch())
        })

        showBottomBar()
    }

    override
    fun onItemClicked(uId: Long) {
        viewModel.currentLocationId = uId
        viewModel.currentMarkerId = NEW_CURRENT_MARKER
        viewModel.currentAreaId = NEW_CURRENT_AREA

        val bundle = Bundle()
        bundle.putInt("content_url", R.string.location_intro_key)
        navController.navigate(R.id.web_view_fragment, bundle)
    }

    override
    fun onMenuClick(view: View, location: Location) {
        viewModel.currentLocationId = location.uId
        navController.navigate(LocationlistFragmentDirections.actionToLocationEdit().actionId)
    }

    override
    fun onDownloadClicked(location: Location) {
        ModuleLoader(activity)
                .setModules(location.moduleId)
                .setCallback(getModuleCallbacks(location))
                .install()
    }

    private fun setLocations() {
        viewModel.getAllLocations()
                .thenAccept { locations ->
                    activity!!.runOnUiThread {
                        fillMylocation(locations.filter { it.isDefaultLocation })
                        adapter.setLocations(locations.filter { !it.isDefaultLocation })
                    }
                }
                .exceptionally {
                    Log.e(TAG, "Unable to fetch all locations.", it)
                    null
                }
    }

    private fun fillMylocation(locations: List<Location>) {
        if (locations.isNotEmpty()) {
            // Only one 'mylocation' allowed
            val location = locations[0]

            binder.mylocation.location = location
            binder.root.setOnClickListener {
                if (location.isLoaded == true) {
                    onItemClicked(location.uId)
                }
            }

            binder.mylocation.locationMenu.setOnClickListener {
                onMenuClick(it, location)
            }
        } else {
            mylocation.visibility = View.GONE
        }
    }

    private fun getModuleCallbacks(location: Location): ModuleLoaderCallback {
        return object : ModuleLoaderCallback {
            override fun onCanceled() {
                // TODO: Re-enable download UI
            }

            override fun onFailed() {
                // TODO: Show error message and re-enable download UI
            }

            override fun onProgress(current: Long, total: Long) {
                // TODO: Update progress UI
            }

            override fun onSuccess() {
                Json.importLocation(context, viewModel, location)
                        .thenAccept { setLocations() }
            }
        }
    }

    companion object {
        private val TAG = LocationlistFragment::class.java.simpleName
    }
}