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

import android.content.Context
import android.util.Log
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import eu.michaelvogt.ar.author.data.AreaVisual
import eu.michaelvogt.ar.author.data.KEY_RESOURCE
import eu.michaelvogt.ar.author.data.TYPE_3DOBJECTONIMAGE
import eu.michaelvogt.ar.author.data.TYPE_DEFAULT
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class AreaNodeBuilder private constructor(private val context: Context, private val areaVisual: AreaVisual) {
    private var scene: Scene? = null

    fun setScene(scene: Scene?): AreaNodeBuilder {
        this.scene = scene
        return this
    }

    fun build(): CompletionStage<Node> {
        when (areaVisual.objectType) {
            TYPE_DEFAULT, TYPE_3DOBJECTONIMAGE -> return future3dObjectOnImage()
            else -> throw IllegalArgumentException("Unknown area object type: " + areaVisual.objectType)
        }
    }

    private fun future3dObjectOnImage(): CompletionStage<Node> {
        val future = CompletableFuture<Node>()
        ModelRenderable.builder()
                .setSource(context, (areaVisual.getDetailValue(KEY_RESOURCE) as Int?)!!)
                .build()
                .thenAccept { renderable -> future.complete(createNode(renderable)) }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to build ModelRenderable.", throwable)
                    null
                }

        return future
    }

    private fun createNode(renderable: Renderable): Node {
        val node = Node()
        node.renderable = renderable
        node.localPosition = areaVisual.position
        node.localRotation = areaVisual.rotation
        node.localScale = areaVisual.scale
        return node
    }

    companion object {
        private val TAG = AreaNodeBuilder::class.java.simpleName

        fun builder(context: Context, areaVisual: AreaVisual): AreaNodeBuilder {
            return AreaNodeBuilder(context, areaVisual)
        }
    }
}
