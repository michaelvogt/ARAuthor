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

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.ar.core.ArCoreApk
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.utils.checkPermissions
import kotlinx.android.synthetic.main.fragment_permission_check.*

class PermissionCheckFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        capable_arcore_btn.setOnClickListener {
            if (ArCoreApk.getInstance().requestInstall(activity, true,
                            ArCoreApk.InstallBehavior.OPTIONAL, ArCoreApk.UserMessageType.USER_ALREADY_INFORMED) == ArCoreApk.InstallStatus.INSTALL_REQUESTED) {
                capable_arcore_btn.text = getString(R.string.arcore_cap_btn_installing)
            }
        }

        req_approved_button.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.popBackStack(R.id.splash_fragment, false)
            navController.navigate(R.id.action_to_location_list)
        }
    }

    override
    fun onResume() {
        super.onResume()

        checkPermissions(activity as Activity, view!!)
    }
}
