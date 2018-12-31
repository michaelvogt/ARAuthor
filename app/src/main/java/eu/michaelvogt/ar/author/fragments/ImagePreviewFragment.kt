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

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.FrameTime
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.utils.Preferences

class ImagePreviewFragment : PreviewFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_imagepreview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(null)

        arFragment.hasMarker = true
        arFragment.arSceneView.scene.addOnUpdateListener { this.onUpdateFrame(it) }

        view.findViewById<View>(R.id.listmarker_fab).setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

        val allowEditPref = Preferences.getPreference(context, R.string.allow_edit_pref, false)
        val fab = view!!.findViewById<View>(R.id.listmarker_fab)
        fab.visibility = if (allowEditPref) View.VISIBLE else View.GONE
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        arFragment.changeGrabbedOrientation(newConfig!!.orientation)
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        arFragment.onUpdate(frameTime)

        val frame = arFragment.arSceneView.arFrame
        val updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)

        for (image in updatedAugmentedImages) {
            val trackingState = arFragment.arSceneView.arFrame.camera.trackingState
            if (trackingState == TrackingState.TRACKING && !handledImages.containsKey(image.name)) {
                handledImages[image.name] = null
                val anchor = image.createAnchor(image.centerPose)
                viewModel.getMarker(Integer.parseInt(image.name).toLong())
                        .thenAccept { marker -> activity!!.runOnUiThread { buildMarkerScene(anchor, marker, image.extentX, image.extentZ) } }
                        .exceptionally { throwable ->
                            Log.e(TAG, "Unable to fetch marker " + image.name, throwable)
                            null
                        }
            } else if (image.trackingState == TrackingState.STOPPED) {
                handledImages.remove(image.name)
            }
        }
    }

    companion object {
        private val TAG = ImagePreviewFragment::class.java.simpleName
    }
}

