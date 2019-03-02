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
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.ux.TransformationSystem
import eu.michaelvogt.ar.author.R
import kotlinx.android.synthetic.main.fragment_ar_replacement.*

class ArReplacement : Fragment() {

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ar_replacement, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Use NodeBuilder
        ModelRenderable.builder()
                .setSource(context, R.raw.big_old_house)
                .build()
                .thenAccept {
                    Node().apply {
                        renderable = it
                        localPosition = Vector3(0f, -3f, -8f)
                        setParent(ar_replacement_scene.scene)
                    }
                }.exceptionally {
                    Log.e(TAG, "AR replacement model couldn't be created", it)
                    null

                }
    }

    override fun onPause() {
        super.onPause()

        ar_replacement_scene.pause()
    }

    override
    fun onResume() {
        super.onResume()

        ar_replacement_scene.resume()
    }

    companion object {
        private val TAG = ArReplacement::class.java.simpleName
    }
}
