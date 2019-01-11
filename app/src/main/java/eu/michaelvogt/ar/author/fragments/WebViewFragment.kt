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

package eu.michaelvogt.ar.author.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.utils.AppWebViewClient
import eu.michaelvogt.ar.author.utils.AppWebViewJs
import eu.michaelvogt.ar.author.utils.FileUtils
import kotlinx.android.synthetic.main.fragment_web_view.*

class WebViewFragment : AppFragment(), View.OnClickListener {

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(content_info) {
            webViewClient = AppWebViewClient()
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.javaScriptEnabled = true

            addJavascriptInterface(AppWebViewJs(activity, navController, viewModel), "Android")
        }

        NavigationUI.setupWithNavController(top_toolbar, navController)

        val content = WebViewFragmentArgs.fromBundle(arguments).contentUrl
        when (content) {
            R.string.about_key -> content_info.loadUrl("file://" + getString(R.string.about_url))
            R.string.location_intro_key -> initLocationIntro()
            else -> content_info.loadUrl("file://" + getString(R.string.error_url))
        }
    }

    override fun onResume() {
        super.onResume()

        hideFab()
        hideBottomBar()
    }

    override
    fun onClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.image_preview_fragment)
    }

    private fun initLocationIntro() {
        val locationId = viewModel.currentLocationId
        viewModel.getLocation(locationId)
                .thenAccept {
                    var path = it.introHtmlPath
                    if (!it.introHtmlPath!!.startsWith(getString(R.string.file_prefix))) {
                        path = FileUtils.getFullPuplicFolderLocalUrl(it.introHtmlPath)
                    }
                    activity!!.runOnUiThread { content_info.loadUrl(path) }
                }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch location $locationId", throwable)
                    null
                }

        viewModel.updateMarkerCache(activity!!, locationId)
    }

    companion object {
        private val TAG = WebViewFragment::class.java.simpleName
    }
}