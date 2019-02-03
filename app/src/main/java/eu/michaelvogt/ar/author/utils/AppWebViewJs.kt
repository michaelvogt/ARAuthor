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

package eu.michaelvogt.ar.author.utils

import android.webkit.JavascriptInterface
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel

/**
 * This class integrates the [WebView] as good as possible into the native environment
 *
 * Functions for example provide data stored in the native environment, handles exceptions or opens
 * fragments.
 */
class AppWebViewJs(
        private val activity: FragmentActivity?,
        private val navController: NavController,
        private val viewModel: AuthorViewModel) {

    @JavascriptInterface
    fun openArView() {
        val importMarkersPref = Preferences.getPreference(activity, R.string.import_marker_images_pref, false)

        if (importMarkersPref && viewModel.markersCache.isNotEmpty())
            navController.navigate(R.id.image_preview_fragment)
        else
            navController.navigate(R.id.touch_preview_fragment)
    }
}
