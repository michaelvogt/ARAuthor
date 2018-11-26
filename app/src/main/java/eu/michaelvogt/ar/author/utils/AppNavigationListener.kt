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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import eu.michaelvogt.ar.author.R
import kotlinx.android.synthetic.main.activity_author.*

class AppNavigationListener(val activity: AppCompatActivity, private val navController: NavController?)
    : NavController.OnNavigatedListener, View.OnClickListener {

    /**
     * Setup application UI based on fragment navigated to
     */
    override
    fun onNavigated(controller: NavController, destination: NavDestination) {
        // Make sure the bottom app bar is scrolled up
        activity.bottom_nav.animate().translationY(0f).interpolator = DecelerateInterpolator(2f)
        activity.bottom_nav.menu.clear()
    }

    /**
     * Handle selections from the navigation menu, shown on the left side of the bottom app bar
     */
    override
    fun onClick(v: View?) {
        val bottomSheetNav = BottomSheetNav()
        bottomSheetNav.show(activity.supportFragmentManager, bottomSheetNav.tag)
        bottomSheetNav.setMenuSelectedListener(object : MenuSelectedListener {
            override fun onMenuSelected(id: Int) {
                when (id) {
                    R.id.actionbar_show_intro -> navController?.navigate(R.id.intro_fragment)
                    R.id.location_list_fragment -> navController?.navigate(R.id.location_list_fragment)
                    R.id.marker_list_fragment -> navController?.navigate(R.id.marker_list_fragment)
                    R.id.area_list_fragment -> navController?.navigate(R.id.area_list_fragment)
                    R.id.actionbar_preferences -> navController?.navigate(R.id.preferences_fragment)
                    R.id.actionbar_feedback -> {
                        val intent = Intent(Intent.ACTION_SENDTO)
                        intent.data = Uri.parse("""
                            mailto:${Uri.encode("ar@michaelvogt.eu")}
                            ?subject=${Uri.encode("Tourist AR Feedback")}
                            &body=${Uri.encode(versionAndDeviceString)}
                        """.trimIndent())
                        activity.startActivity(Intent.createChooser(intent, "Choose an email client"))
                    }
                    R.id.actionbar_about -> {
                        val bundle = Bundle()
                        bundle.putInt("content_url", R.string.about_key)
                        navController?.navigate(R.id.web_view_fragment, bundle)
                    }
                }
            }
        })
    }
}