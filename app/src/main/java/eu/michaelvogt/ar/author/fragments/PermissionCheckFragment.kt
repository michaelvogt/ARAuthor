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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.michaelvogt.ar.author.AuthorActivity
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.utils.getPreference
import eu.michaelvogt.ar.author.utils.setPreference
import kotlinx.android.synthetic.main.fragment_permission_check.*

class PermissionCheckFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isCheckedPreference = getPreference(
                activity,
                resources.getString((R.string.checkbox_use_arcore_key)),
                resources.getBoolean(R.bool.use_arcore_key_default))

        use_arcore_check.isChecked = isCheckedPreference
        capable_arcore_btn.isEnabled = isCheckedPreference

        use_arcore_check.setOnCheckedChangeListener { _, isChecked ->
            capable_arcore_btn.isEnabled = isChecked
            setPreference(activity, resources.getString(R.string.checkbox_use_arcore_key), isChecked)
            (activity as AuthorActivity).checkArcorePermission()
        }
    }

    override
    fun onResume() {
        super.onResume()

        (activity as AuthorActivity).checkPermissions()
    }
}
