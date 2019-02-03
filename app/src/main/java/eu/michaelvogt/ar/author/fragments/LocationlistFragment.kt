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
import eu.michaelvogt.ar.author.databinding.FragmentLocationlistBinding
import eu.michaelvogt.ar.author.fragments.adapters.LocationListAdapter
import eu.michaelvogt.ar.author.utils.*
import kotlinx.android.synthetic.main.fragment_locationlist.*
import org.json.JSONObject

/**
 * View to list the [Location]s provided by the view model.
 *
 * Tab on a list item navigates to the web view, which loads the content of the URL stored in
 * [Location.introHtmlPath].
 */
class LocationlistFragment : AppFragment(), CardMenuListener {
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

        val appBarConfiguration = AppBarConfiguration.Builder(R.id.location_list_fragment).build()
        NavigationUI.setupWithNavController(top_toolbar, navController, appBarConfiguration)

        setupToolbar(R.menu.toolbar_locationlist_menu, Toolbar.OnMenuItemClickListener {
            InfoPrompt.showOverlayInfo(this,
                    R.id.toolbar_location_info, R.string.location_info_primary, R.string.location_info_secondary)
        })
    }

    override fun onResume() {
        super.onResume()

        setupFab(R.drawable.ic_search_black_24dp, View.OnClickListener {
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
/*
        // Can't be used currently, because of an incompatibility of play.core and sceneform
        // https://github.com/google-ar/sceneform-android-sdk/issues/507

        val splitManager = SplitInstallManagerFactory.create(context)
        val splitRequest = SplitInstallRequest.newBuilder().addModule(moduleId).build()

        splitManager.startInstall(splitRequest)
                .addOnSuccessListener {
                    println("Module isLoaded. Session: $it")
                }
                .addOnFailureListener {
                    println("LocationListFragment error: $it")
                }

        splitManager.registerListener { state ->
            state.moduleNames().forEach {
                when (state.status()) {
                    SplitInstallSessionStatus.PENDING -> println("pending")
                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                        println("confirmation")
                        activity?.startIntentSender(
                                state.resolutionIntent().getIntentSender(),
                                null, 0, 0, 0)
                    }
                    SplitInstallSessionStatus.DOWNLOADING -> println("downloading")
                    SplitInstallSessionStatus.INSTALLING -> println("installing")
                    SplitInstallSessionStatus.INSTALLED -> println("installed")
                    SplitInstallSessionStatus.FAILED -> println("failed")
                    SplitInstallSessionStatus.CANCELING -> println("canceling")
                    SplitInstallSessionStatus.CANCELED -> println("canceled")
                }
            }
        }
*/
        // Module is already isLoaded and active until Sceneform bug is fixed

        // import module json file
        // save in database
        // reload location list

        val assetManager = activity?.assets

        if (assetManager != null) {
            val moduleId = location.moduleId.also {
                when (it) {
                    null, "" -> {
                        Log.e(TAG, "Module ID of location ${location.name} is missing. Required to access content")
                    }
                }
            }

            val reader = assetManager.open("$moduleId.json").bufferedReader()
            val contentString = reader.readText()
            reader.close()

            if (location.isLoaded != null && location.isLoaded == false) {
                // Insert content
                val locationObject = JSONObject(contentString).getJSONObject("location")
                val groupArray = locationObject.getJSONArray("groups")

                for (groupIndex in 0 until groupArray.length()) {
                    val groupObject = groupArray.getJSONObject(groupIndex)

                    viewModel.insertTitleGroup(TitleGroup(groupObject.getString("name"))).thenAccept { groupId ->
                        val markersArray = groupObject.getJSONArray("markers")

                        for (markerIndex in 0 until markersArray.length()) {
                            val markerObject = markersArray.getJSONObject(markerIndex)

                            viewModel.insertMarker(Marker(location.uId, groupId, markerObject.getString("title"))).thenAccept { markerId ->
                                val areasArray = markerObject.getJSONArray("areas")

                                for (areaIndex in 0 until areasArray.length()) {
                                    val areaObject = areasArray.getJSONObject(areaIndex)

                                    viewModel.insertArea(Area(areaObject.getString("title"), areaObject.getInt("object_type"))).thenAccept { areaId ->
                                        viewModel.insertMarkerArea(MarkerArea(markerId, areaId))
                                    }.exceptionally {
                                        Log.e(TAG, "Unable to insert area.", it)
                                        null
                                    }
                                }

                                location.isLoaded = true
                                viewModel.updateLocation(location).thenAccept {
                                    setLocations()
                                }
                            }.exceptionally {
                                Log.e(TAG, "Unable to insert marker.", it)
                                null
                            }
                        }
                    }.exceptionally {
                        Log.e(TAG, "Unable to insert group", it)
                        null
                    }
                }
            } else {
                // TODO: Update content
            }
        }
    }

    private fun setLocations() {
        viewModel.getAllLocations()
                .thenAccept { locations ->
                    activity!!.runOnUiThread {
                        fillMylocation(locations.filter { it.isDefaultLocation })
                        adapter.setLocations(locations.filter { !it.isDefaultLocation })
                    }
                }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch all locations.", throwable)
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

    companion object {
        private val TAG = LocationlistFragment::class.java.simpleName
    }
}