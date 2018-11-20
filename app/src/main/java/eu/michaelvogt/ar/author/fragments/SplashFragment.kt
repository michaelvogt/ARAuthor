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
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.utils.meetsPermissionRequirements

/*
    Splash screen fragment

    Functions as gate into the application. As long as the required permissions aren't met, it
    navigates to the info screen with explanations and access to the permissions settings.

    When all permissions are met, the application is started.
 */
class SplashFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    /**
     * Checks if the necessary permissions needed for the applications are granted. If met,
     * advances into the application, otherwise advances to an intro screen with permission
     * info.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var action = SplashFragmentDirections.actionToLocationList().actionId
        if (!meetsPermissionRequirements(context)) {
            action = SplashFragmentDirections.actionToIntro().actionId
        }

        Navigation.findNavController(view).navigate(action)
    }
}
