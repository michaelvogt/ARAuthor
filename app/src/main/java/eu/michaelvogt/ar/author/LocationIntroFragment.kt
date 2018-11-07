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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.data.MARKERS_AND_TITLES
import eu.michaelvogt.ar.author.utils.FileUtils

class LocationIntroFragment : Fragment(), View.OnClickListener {

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_locationintro, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        val contentView = view.findViewById<WebView>(R.id.content_info)
        contentView.webViewClient = WebViewClient()
        contentView.settings.builtInZoomControls = true
        contentView.settings.displayZoomControls = false
        contentView.settings.javaScriptEnabled = true

        val locationId = viewModel.currentLocationId

        viewModel.getLocation(locationId)
                .thenAccept { location -> activity!!.runOnUiThread { initWebView(location, contentView) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch location $locationId", throwable)
                    null
                }

        // Due to the way the AR images database needs to be initialized, and the markers are
        // delivered asynchronously from the data database, the markers need to be cached beforehand
        viewModel.getMarkersForLocation(locationId, MARKERS_AND_TITLES)
                .thenAccept { locations -> activity!!.runOnUiThread { viewModel.markersCache = locations } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch markers for location $locationId", throwable)
                    null
                }

        val fab = view.findViewById<View>(R.id.fab_map)
        fab.setOnClickListener(this)
    }

    private fun initWebView(location: Location, contentView: WebView) {
        var path: String? = null
        try {
            path = FileUtils.getFullPuplicFolderLocalUrl(location.introHtmlPath)
        } catch (e: Exception) {
            Log.e(TAG, "Not able to load location intro of " + location.name, e)
        }

        contentView.loadUrl(path)
    }

    override
    fun onClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_preview_markers)
    }

    companion object {
        private val TAG = LocationIntroFragment::class.java.simpleName
    }
}