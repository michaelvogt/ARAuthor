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

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.MARKERS_AND_TITLES
import eu.michaelvogt.ar.author.utils.AppWebViewClient
import eu.michaelvogt.ar.author.utils.FileUtils
import kotlinx.android.synthetic.main.fragment_web_view.*

class WebViewFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: AuthorViewModel

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        content_info.webViewClient = AppWebViewClient()
        content_info.settings.builtInZoomControls = true
        content_info.settings.displayZoomControls = false
        content_info.settings.javaScriptEnabled = true

        val content = WebViewFragmentArgs.fromBundle(arguments).contentUrl
        when (content) {
            R.string.about_key -> content_info.loadUrl(getString(R.string.about_url))
            R.string.location_intro_key -> initLocationIntro()
            else -> content_info.loadUrl(getString(R.string.error_url))
        }
    }

    override
    fun onClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.markerPreviewFragment)
    }

    private fun initLocationIntro() {
        val locationId = viewModel.currentLocationId
        viewModel.getLocation(locationId)
                .thenAccept {
                    val path = FileUtils.getFullPuplicFolderLocalUrl(it.introHtmlPath)
                    activity!!.runOnUiThread { content_info.loadUrl(path) }
                }
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

    }

    companion object {
        private val TAG = WebViewFragment::class.java.simpleName
    }
}