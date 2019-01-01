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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Marker
import eu.michaelvogt.ar.author.utils.Preferences
import kotlinx.android.synthetic.main.fragment_tabpreview.*

class TabPreviewFragment : PreviewFragment() {
    private var isSceneDropped: Boolean = false

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tabpreview, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arFragment.planeDiscoveryController.show()
        arFragment.hasMarker = false
        arFragment.setOnTapArPlaneListener { hitResult, plane, _ ->
            onTapArPlane(hitResult, plane)
        }

        listmarker_fab.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.marker_list_fragment)
        )
    }

    override fun onResume() {
        super.onResume()

        hideBottomBar()
        hideFab()

        val allowEdit = Preferences.getPreference(context, R.string.allow_edit_pref, false)
        (listmarker_fab as View).visibility = if (allowEdit) View.VISIBLE else View.GONE
    }

    private fun onTapArPlane(hitResult: HitResult, plane: Plane) {
        if (!isSceneDropped) {
            if (plane.trackingState == TrackingState.TRACKING) {
                isSceneDropped = true

                val anchor = hitResult.createAnchor()

                viewModel.getMarker(viewModel.currentMarkerId)
                        .thenAccept {
                            var marker = it
                            if (marker == null) {
                                marker = Marker.getDefaultMarker()
                            }
                            activity!!.runOnUiThread {
                                buildMarkerScene(anchor, marker, marker.size.x, marker.size.z)
                            }
                        }
                        .exceptionally {
                            Log.e(TAG, "Unable to fetch marker " + viewModel.currentMarkerId, it)
                            null
                        }
            } else if (plane.trackingState == TrackingState.STOPPED) {
                isSceneDropped = false
            }
        }
    }

    companion object {
        private val TAG = ImagePreviewFragment::class.java.simpleName
    }
}

