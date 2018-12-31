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

package eu.michaelvogt.ar.author.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceFragmentCompat
import eu.michaelvogt.ar.author.R
import kotlinx.android.synthetic.main.activity_author.*

class PreferencesFragment : PreferenceFragmentCompat() {
    private lateinit var navController: NavController

    override
    fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    override
    fun onResume() {
        super.onResume()

        activity!!.bottom_nav.visibility = View.GONE

        with(activity!!.bottom_nav_fab) {
            (this as View).visibility = View.VISIBLE
            setImageResource(R.drawable.ic_arrow_back_black_24dp)
            setOnClickListener {
                navController.popBackStack()
            }
        }
    }
}