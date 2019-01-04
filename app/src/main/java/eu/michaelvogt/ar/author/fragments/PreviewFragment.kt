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
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.ar.core.Anchor
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*
import eu.michaelvogt.ar.author.nodes.AreaNode
import eu.michaelvogt.ar.author.nodes.AreaNodeBuilder
import eu.michaelvogt.ar.author.nodes.AuthorAnchorNode
import eu.michaelvogt.ar.author.utils.SceneViewCallback
import java.util.*
import java.util.function.Consumer

open class PreviewFragment : AppFragment() {

    protected lateinit var container: ViewGroup
    protected lateinit var arFragment: LoopArFragment

    protected var handledImages: MutableMap<String, Node?> = HashMap()
    protected var cameraFacingNodes: MutableList<Node> = ArrayList()

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        container = view as ViewGroup
        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        arFragment = childFragmentManager.findFragmentById(R.id.ar_fragment) as LoopArFragment
        arFragment.arSceneView.scene.addOnUpdateListener { this.onUpdateFrame(it) }

        arFragment.planeFindingMode = arguments!!.getString("plane_finding_mode")
        arFragment.updateMode = arguments!!.getString("update_mode")
        arFragment.focusMode = arguments!!.getString("focus_mode")
        arFragment.lightEstimation = arguments!!.getString("light_estimation")
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        arFragment.onUpdate(frameTime)

        cameraFacingNodes.forEach { node ->
            val cameraPosition = arFragment.arSceneView.scene.camera.worldPosition
            val cardPosition = node.worldPosition
            val direction = Vector3.subtract(cameraPosition, cardPosition)
            val lookRotation = Quaternion.lookRotation(direction, Vector3.up())
            node.worldRotation = lookRotation
        }

        //    if (modelPoseOnPlaneListeners.size() != 0) {
        //      for (ModelPoseOnPlaneListener listener : modelPoseOnPlaneListeners) {
        //        if (listener.onPlane()) {
        //          modelPoseOnPlaneListeners.remove(listener);
        //        }
        //      }
        //    }
    }

    protected fun buildMarkerScene(anchor: Anchor, marker: Marker, backgroundWidth: Float, backgroundHeight: Float) {
        val anchorNode = AuthorAnchorNode(anchor, context, container, EventCallback())
        anchorNode.setParent(arFragment.arSceneView.scene)

        if (marker.isShowBackground) {
            buildArea(anchorNode, AreaVisual.getBackgroundArea(marker, marker.backgroundImagePath), Consumer {
                buildAreas(it, marker.uId, backgroundHeight, backgroundWidth)
                it.setLookDirection(Vector3.up(), anchorNode.up)
            })
        } else {
            buildAreas(anchorNode, marker.uId, backgroundHeight, backgroundWidth)
        }
    }

    private fun buildAreas(anchorNode: Node, markerId: Long, backgroundHeight: Float, backgroundWidth: Float) {
        viewModel.getAreaVisualsForMarker(markerId, arrayOf(GROUP_START, GROUP_ALL))
                .thenAccept { areaVisuals ->
                    if (areaVisuals.size > 0) {
                        areaVisuals.forEach { areaVisual -> buildArea(anchorNode, areaVisual, null) }
                    } else {
                        // Build a default area for demo purposes
                        buildArea(anchorNode, AreaVisual.getDefaultAreaVisual(backgroundHeight, backgroundWidth), null)
                    }
                }.exceptionally { throwable ->
                    Log.e(TAG, "Unable to build Areas: $markerId", throwable)
                    null
                }
    }

    private fun buildArea(anchorNode: Node, areaVisual: AreaVisual, fn: Consumer<Node>?) {
        this.activity!!.runOnUiThread {
            AreaNodeBuilder
                    .builder(activity, areaVisual)
                    .build()!!
                    .thenAccept { node ->
                        node.setParent(anchorNode)

                        fn?.accept(node)

                        if (node is AreaNode && node.isCameraFacing) {
                            cameraFacingNodes.add(node)
                        }

                        Log.i(TAG, "Area successfully created " + areaVisual.title)
                    }
                    .exceptionally { throwable ->
                        Log.e(TAG, "Unable to build Area: " + areaVisual.title, throwable)
                        null
                    }
        }
    }

    inner class EventCallback : SceneViewCallback {
        override fun pause() {
            arFragment.arSceneView.pause()
        }

        @Throws(CameraNotAvailableException::class)
        override fun resume() {
            arFragment.arSceneView.resume()
        }
    }

    companion object {
        private val TAG = PreviewFragment::class.java.simpleName
    }
}
