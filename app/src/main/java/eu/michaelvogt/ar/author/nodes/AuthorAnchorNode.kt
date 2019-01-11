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

package eu.michaelvogt.ar.author.nodes

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.ar.core.Anchor
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*
import eu.michaelvogt.ar.author.fragments.PreviewFragment
import eu.michaelvogt.ar.author.fragments.support.Slider
import eu.michaelvogt.ar.author.utils.ToggleSlideTextHandler

class AuthorAnchorNode(
        anchor: Anchor,
        private val context: Context,
        private val containerView: ViewGroup,
        private val eventCallback: PreviewFragment.EventCallback) : AnchorNode(anchor) {
    private var grabContainer: ViewGroup? = null

    private var currentScaleIndex = 1

    private val currentContentView: View?
        get() {
            val viewModel = ViewModelProviders.of(context as FragmentActivity).get(AuthorViewModel::class.java)
            val currentMainContent = viewModel.currentMainContentId

            val result = findInHierarchy { node -> node.name == currentMainContent }
            return if (result == null) null else (result.renderable as ViewRenderable).view
        }

    fun changeGrabbedOrientation(configOrientation: Int) {
        if (grabContainer != null && grabContainer!!.findViewById<View>(R.id.slider) != null) {
            setupSlider(grabContainer!!, if (configOrientation == Configuration.ORIENTATION_LANDSCAPE)
                R.layout.view_slider_grab_landscape
            else
                R.layout.view_slider_grab_portrait)
        }
    }

    override fun onTouchEvent(hitTestResult: HitTestResult, motionEvent: MotionEvent): Boolean {
        if (hitTestResult.node == null) {
            return false
        }

        if (motionEvent.actionMasked == MotionEvent.ACTION_UP) {
            val node = hitTestResult.node
            if (node is EventSender) {
                val eventNode = node as EventSender

                val eventDetails = eventNode.eventDetails
                for (index in 0 until eventDetails.size()) {
                    val eventDetail = eventDetails.valueAt(index)
                    when (eventDetail.type) {
                        EVENT_HIDECONTENT -> handleHideContent()
                        EVENT_GRABCONTENT -> handleGrabContent()
                        EVENT_SETMAINCONTENT -> handleSetMainContent(eventDetail)
                        EVENT_ZOOM -> handleZoom(eventDetail)
                        EVENT_SCALE -> handleScale(eventDetail)
                        else -> broadcastEvent(eventDetail, motionEvent)
                    }
                }
            }
        }
        return true
    }

    private fun handleSetMainContent(eventDetail: EventDetail) {
        val viewModel = ViewModelProviders.of(context as FragmentActivity).get(AuthorViewModel::class.java)
        viewModel.currentMainContentId = eventDetail.anyValue as String
    }

    private fun handleHideContent() {
        callOnHierarchy { node ->
            if (node is AreaNode && node.isContentNode) {
                node.hide()
            }
        }
    }

    private fun handleScale(eventDetail: EventDetail) {
        currentScaleIndex = if (currentScaleIndex + 1 >= (eventDetail.anyValue as List<Float>).size) 0 else ++currentScaleIndex

        val background = findInHierarchy { node -> node.name == BACKGROUNDAREATITLE }
        if (background != null) {
            val scale = (eventDetail.anyValue as List<Float>)[currentScaleIndex]
            background.localScale = Vector3(scale, 1f, scale)
        } else {
            // TODO: Handle scale for non background scenes
        }
    }

    private fun handleZoom(eventDetail: EventDetail) {
        val viewModel = ViewModelProviders.of(context as FragmentActivity).get(AuthorViewModel::class.java)
        val areaId = eventDetail.anyValue as Long
        viewModel.getAreaVisual(areaId)
                .thenAccept { areaVisual ->
                    val background = findInHierarchy { node -> node.name == BACKGROUNDAREATITLE }

                    context.runOnUiThread {
                        AreaNodeBuilder.builder(context, areaVisual)
                                .setScene(scene)
                                .build()
                                .thenAccept { node -> node.setParent(background) }
                                .exceptionally { throwable ->
                                    Log.e(TAG, "Unable to zoom area.", throwable)
                                    null
                                }
                    }
                }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to fetch area visual $areaId", throwable)
                    null
                }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleGrabContent() {
        // TODO: display text in WebView instead
        // TODO: add animations
        // TODO: improve layout
        val content = currentContentView
        val contentParent = content!!.parent as ViewGroup

        grabContainer = containerView.findViewById(R.id.grab_container)
        val fab = containerView.findViewById<View>(R.id.listmarker_fab)

        val closeButton = containerView.findViewById<View>(R.id.grab_close)
        closeButton.setOnTouchListener { _, motionEvent ->
            if (motionEvent.actionMasked == MotionEvent.ACTION_UP) {
                try {
                    (context as FragmentActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
                    eventCallback.resume()
                } catch (e: CameraNotAvailableException) {
                    // TODO: get to know how this can happen and handle it appropriately
                    e.printStackTrace()
                }

                if (content.findViewById<View>(R.id.slider) == null) {
                    grabContainer!!.removeView(content)
                    contentParent.addView(content)
                }

                grabContainer!!.visibility = View.GONE
                grabContainer = null

                closeButton.visibility = View.GONE
                fab.visibility = View.VISIBLE
            }
            true
        }

        if (content.findViewById<View>(R.id.slider) != null) {
            // Uses separate layout for the grabbed view of slider (had trouble to reuse the view from the node)
            setupSlider(content, R.layout.view_slider_grab_portrait)
        } else {
            contentParent.removeView(content)
            grabContainer!!.addView(content)
        }

        grabContainer!!.visibility = View.VISIBLE
        closeButton.visibility = View.VISIBLE
        fab.visibility = View.INVISIBLE

        eventCallback.pause()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSlider(content: View, sliderResource: Int) {
        val slider = content.findViewById<Slider>(R.id.slider)
        val slides = slider.slides

        grabContainer!!.removeAllViews()
        val grabView = LayoutInflater.from(context).inflate(sliderResource, grabContainer)
        val grabSlider = grabView.findViewById<Slider>(R.id.slider)
        val sliderText = grabView.findViewById<View>(R.id.slider_text)

        grabSlider.setSlides(slides, null)
        grabSlider.setOnTouchListener(ToggleSlideTextHandler(context, sliderText))

        (context as FragmentActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    private fun broadcastEvent(eventDetail: EventDetail, motionEvent: MotionEvent) {
        callOnHierarchy { node ->
            if (node is EventHandler) {
                (node as EventHandler).handleEvent(eventDetail, motionEvent)
            }
        }
    }

    companion object {
        private val TAG = AuthorAnchorNode::class.java.simpleName
    }
}
