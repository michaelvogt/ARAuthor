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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
    fun onDownloadClicked(location: Location, download: View,
                          progress: ProgressBar, label: View, size: View, message: TextView) {
        ModuleLoader(activity)
                .setModules(location.moduleId)
                .setCallback(getModuleCallbacks(location, download, progress, label, size, message))
                .install()

        // TODO: Comment when deactivating dynamic modules
        download.visibility = View.GONE
        progress.visibility = View.VISIBLE
        progress.isIndeterminate = true
    }

    private fun setLocations() {
        viewModel.getAllLocations()
                .thenAccept { locations ->
                    activity!!.runOnUiThread {
                        fillMyLocation(locations.filter { it.isDefaultLocation })
                        adapter.setLocations(locations.filter { !it.isDefaultLocation })
                    }
                }
                .exceptionally {
                    Log.e(TAG, "Unable to fetch all locations.", it)
                    null
                }
    }

    private fun fillMyLocation(locations: List<Location>) {
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

    private fun getModuleCallbacks(location: Location, download: View, progress: ProgressBar,
                                   sizeLabel: View, size: View, messageDisplay: TextView): ModuleLoaderCallback {
        return object : ModuleLoaderCallback {
            override
            fun onCanceled() {
                download.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }

            override
            fun onFailed(message: String?) {
                showMessage(message, sizeLabel, size, messageDisplay)
            }

            override fun onInfo(info: String) {
                showInfo(info)
            }

            override
            fun onProgress(current: Long, total: Long) {
                progress.isIndeterminate = false
                progress.max = total.toInt()
                progress.setProgress(current.toInt(), true)
            }

            override
            fun onSuccess() {
                Json.importLocation(context, viewModel, location)
                        .thenAccept { setLocations() }
            }
        }
    }

    private fun showMessage(message: String?, sizeLabel: View, size: View, messageDisplay: View) {
        sizeLabel.visibility = View.GONE
        size.visibility = View.GONE
        messageDisplay.visibility = View.VISIBLE
        (messageDisplay as TextView).text = message

        Run.after(3000) {
            sizeLabel.visibility = View.VISIBLE
            size.visibility = View.VISIBLE
            messageDisplay.visibility = View.GONE
        }
    }

    private fun showInfo(message: String?) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        toast.show()
    }

    companion object {
        private val TAG = LocationlistFragment::class.java.simpleName
    }
}