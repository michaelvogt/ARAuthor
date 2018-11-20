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

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.google.ar.core.ArCoreApk
import eu.michaelvogt.ar.author.data.AppDatabase
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.fragments.LocationlistFragmentDirections
import eu.michaelvogt.ar.author.utils.*
import kotlinx.android.synthetic.main.activity_author.*
import kotlinx.android.synthetic.main.fragment_permission_check.*

/**
 * Single activity of the application. Navigation is done through the Navigation Jetpack
 * Architecture component.
 */
class AuthorActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    /**
     * Do application wide initialisation for UI, database and navigation handler.
     */
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        setSupportActionBar(bottom_nav)

        navController.addOnNavigatedListener { _, destination ->
            handleBottomAppbarVisibility(destination)

            val fab = findViewById<View>(R.id.bottom_nav_fab)   // Can't be replaced by generated id

            when (destination.id) {
                R.id.location_list_fragment -> {
                    fab.setOnClickListener {
                        navController.navigate(LocationlistFragmentDirections.actionToLocationEdit())
                    }
                }
                else -> {
                    fab.visibility = View.GONE
                    fab.setOnClickListener(null)
                }
            }
        }

        val database = AppDatabase.getDatabase(applicationContext)
        val viewModel = ViewModelProviders.of(this).get(AuthorViewModel::class.java)
        AppDatabase.PopulateDbAsync(database!!) { viewModel.locationLoadTrigger.setValue(0) }.execute()

        // Pre-Determine the availability of ARCore on the device, to have immediate access when needed
        ArCoreApk.getInstance().checkAvailability(this)
    }

    /**
     * Provide the application menu items, shown on the right side of the bottom app bar.
     */
    override
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    /**
     * Handle selections of the application menu
     */
    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                handleNavigationMenu()
                true
            }
            R.id.actionbar_feedback -> {
                true
            }
            R.id.actionbar_preferences -> {
                navController.navigate(R.id.preferences_fragment)
                true
            }
            R.id.actionbar_show_intro -> {
                navController.navigate(LocationlistFragmentDirections.actionToIntro())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Callback into the application from the permission dialogs. Triggers the update of the UI
     * according the results.
     */
    override
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                        permissionApproved(CAMERA_PERMISSION, camera_req_btn, resources.getString(R.string.camera_req_btn_approved))
                    else -> {
                        permissionDenied(this, CAMERA_PERMISSION, camera_req_btn, CAMERA_PERMISSION_CODE)
                    }
                }
            }

            STORAGE_PERMISSION_CODE -> {
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                        permissionApproved(STORAGE_PERMISSION, storage_req_btn, resources.getString(R.string.storage_req_btn_approved))
                    else -> {
                        permissionDenied(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, storage_req_btn, STORAGE_PERMISSION_CODE)
                    }
                }
            }
        }

        checkPermissions(this, app_layout)
    }

    /**
     * The bottom app bar isn't required on every screen. This is handled globally here.
     */
    private fun handleBottomAppbarVisibility(destination: NavDestination) {
        val fab = findViewById<View>(R.id.bottom_nav_fab)   // Can't be replaced by generated id

        when (destination.id) {
            R.id.intro_fragment,
            R.id.permission_check_fragment,
            R.id.splash_fragment -> {
                bottom_nav.visibility = View.GONE
                fab.visibility = View.GONE
            }
            else -> {
                bottom_nav.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Handle selections from the navigation menu, shown on the left side of the bottom app bar
     */
    private fun handleNavigationMenu() {
        val bottomSheetNav = BottomSheetNav()
        bottomSheetNav.show(supportFragmentManager, bottomSheetNav.tag)
        bottomSheetNav.setMenuSelectedListener(object : MenuSelectedListener {
            override fun onMenuSelected(id: Int) {
                when (id) {
                    R.id.location_list_fragment -> navController.navigate(R.id.location_list_fragment)
                    R.id.marker_list_fragment -> navController.navigate(R.id.marker_list_fragment)
                    R.id.area_list_fragment -> navController.navigate(R.id.area_list_fragment)
                }
            }
        })
    }
}