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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.ar.core.ArCoreApk
import eu.michaelvogt.ar.author.data.AppDatabase
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.fragments.LocationlistFragmentDirections
import eu.michaelvogt.ar.author.utils.AppNavigatedListener
import eu.michaelvogt.ar.author.utils.BottomSheetNav
import eu.michaelvogt.ar.author.utils.MenuSelectedListener
import eu.michaelvogt.ar.author.utils.handleRequestPermissionsResult
import kotlinx.android.synthetic.main.activity_author.*

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
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_author)

        val viewModel = ViewModelProviders.of(this).get(AuthorViewModel::class.java)
        val database = AppDatabase.getDatabase(applicationContext)
        AppDatabase.PopulateDbAsync(database!!) { viewModel.locationLoadTrigger.setValue(0) }.execute()

        setSupportActionBar(bottom_nav)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.addOnNavigatedListener(AppNavigatedListener(this))

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
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("""
                            mailto:${Uri.encode("ar@michaelvogt.eu")}
                            ?subject=${Uri.encode("Tourist AR Feedback")}
                            &body=${Uri.encode(getVersionAndDevice())}
                        """.trimIndent())
                startActivity(Intent.createChooser(intent, "Choose an email client"))
                true
            }
            R.id.actionbar_about -> {
                val bundle = Bundle()
                bundle.putInt("content_url", R.string.about_key)
                navController.navigate(R.id.web_view_fragment, bundle)
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

        handleRequestPermissionsResult(this, requestCode, grantResults)
    }

    private fun getVersionAndDevice(): String? {
        return "Version: ${BuildConfig.VERSION_NAME}<${BuildConfig.VERSION_CODE}>\n" +
                "Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
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