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

package eu.michaelvogt.ar.author

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.ar.core.ArCoreApk
import eu.michaelvogt.ar.author.data.AppDatabase
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.utils.AppNavigationListener
import eu.michaelvogt.ar.author.utils.handleRequestPermissionsResult
import kotlinx.android.synthetic.main.activity_author.*

/**
 * Single activity of the application. Navigation is done through the Navigation Jetpack
 * Architecture component.
 */
class AuthorActivity : AppCompatActivity() {

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

        if (database != null) {
            AppDatabase.PopulateDbAsync(database) { viewModel.locationLoadTrigger.setValue(0) }.execute()
        }

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(AppNavigationListener(this, null))

        // Handler for navigation menu selections
        bottom_nav.setNavigationOnClickListener(AppNavigationListener(this, navController))

        // Pre-Determine the availability of ARCore on the device,
        // to have immediate access when needed
        ArCoreApk.getInstance().checkAvailability(this)
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

    companion object {
        private var TAG = AuthorActivity::class.java.simpleName
    }
}