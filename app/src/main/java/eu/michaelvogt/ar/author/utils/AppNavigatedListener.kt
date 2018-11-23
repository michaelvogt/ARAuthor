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

package eu.michaelvogt.ar.author.utils

import android.app.Activity
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.fragments.LocationlistFragmentDirections
import kotlinx.android.synthetic.main.activity_author.*

class AppNavigatedListener(val activity: Activity) : NavController.OnNavigatedListener {
    override fun onNavigated(controller: NavController, destination: NavDestination) {
        handleBottomAppbarVisibility(destination)

        val fab = activity.bottom_nav_fab

        when (destination.id) {
            R.id.location_list_fragment -> {
                fab.show()
                fab.setOnClickListener {
                    controller.navigate(LocationlistFragmentDirections.actionToLocationEdit())
                }
            }
            else -> {
                fab.hide()
                fab.setOnClickListener(null)
            }
        }
    }

    /**
     * The bottom app bar isn't required on every screen. This is handled globally here.
     */
    private fun handleBottomAppbarVisibility(destination: NavDestination) {
        when (destination.id) {
            R.id.intro_fragment,
            R.id.permission_check_fragment,
            R.id.splash_fragment -> {
                activity.bottom_nav.visibility = View.GONE
                activity.bottom_nav_fab.hide()
            }
            else -> {
                activity.bottom_nav.visibility = View.VISIBLE
                activity.bottom_nav.hideOnScroll = true
                activity.bottom_nav_fab.show()
            }
        }
    }
}