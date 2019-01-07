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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.Log
import android.util.SparseArray
import androidx.core.content.ContextCompat
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.Texture
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*
import eu.michaelvogt.ar.author.utils.FileUtils
import eu.michaelvogt.ar.author.utils.ImageUtils
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class ImageNode private constructor(context: Context, areaVisual: AreaVisual) : AreaNode(context, areaVisual), EventSender {

    private var isFadeIn = true

    fun setIsCameraFacing(isCameraFacing: Boolean): ImageNode {
        this.isCameraFacing = isCameraFacing
        return this
    }

    fun setRenderPriority(renderPriority: Int): ImageNode {
        this.renderPriority = renderPriority
        return this
    }

    fun build(): CompletionStage<Node> {
        val future = CompletableFuture<Node>()
        val futureTexture: CompletableFuture<Texture>

        when {
            areaVisual.hasDetail(KEY_IMAGEPATH) -> {
                val bitmap = ImageUtils.bitmapFromVisualDetail(context, areaVisual.getDetailValue(KEY_IMAGEPATH) as String?)

                futureTexture = Texture.builder()
                        .setSource(bitmap)
                        .setUsage(Texture.Usage.COLOR)
                        .build()
                        .exceptionally { throwable ->
                            Log.d(TAG, "Could not load texture image: " + areaVisual.getDetailValue(KEY_IMAGEPATH)!!, throwable)
                            null
                        }

            }
            areaVisual.hasDetail(KEY_IMAGERESOURCE) -> {
                val textureResource = areaVisual.getDetailValue(KEY_IMAGERESOURCE, R.drawable.ic_launcher) as Int
                val bitmap = getBitmapFromVectorDrawable(context, textureResource)

                futureTexture = Texture.builder()
                        .setSource(bitmap)
                        .setUsage(Texture.Usage.COLOR)
                        .build()
                        .exceptionally { throwable ->
                            Log.d(TAG, "Could not load texture resource: " + areaVisual.getDetailValue(KEY_IMAGERESOURCE)!!, throwable)
                            null
                        }

            }
            else -> throw IllegalArgumentException("Missing AreaVisual IMAGEPATH or IMAGERESOURCE")
        }

        futureTexture
                .thenAccept { texture ->
                    ModelRenderable.builder()
                            // Keep default model as resource
                            .setSource(context, AreaNode.CUSTOM_MATERIAL_TEMP)
                            .build()
                            .thenAccept { temp ->
                                // TODO: Hack - fix when custom material can be created #196
                                val material = temp.material
                                material.setTexture("primary", texture)
                                areaVisual.apply(material)

                                setupFadeAnimation(material)

                                val renderable = ShapeFactory.makeCube(areaVisual.size, areaVisual.zeroPoint, material)
                                areaVisual.apply(renderable)

                                // Needs to be set, bacause Sceneform has a layering problem with transparent objects
                                // https://github.com/google-ar/sceneform-android-sdk/issues/285#issuecomment-420730274
                                renderable.renderPriority = renderPriority

                                setRenderable(renderable)

                                Log.i(TAG, "ImageNode successfully created")
                                future.complete(this)
                            }
                            .exceptionally { throwable ->
                                Log.d(TAG, "Could not create model " + AreaNode.CUSTOM_MATERIAL_TEMP, throwable)
                                null
                            }
                }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to build texture.", throwable)
                    null
                }

        return future
    }

    private fun setupFadeAnimation(material: Material) {
        if (areaVisual.hasDetail(KEY_SECONDARYIMAGEPATH)) {
            val textureFilePath = FileUtils.getFullPuplicFolderPath(
                    areaVisual.getDetailValue(KEY_SECONDARYIMAGEPATH, "Touristar/default/images/") as String)

            Texture.builder()
                    .setSource(BitmapFactory.decodeFile(textureFilePath))
                    .setUsage(Texture.Usage.COLOR)
                    .build()
                    .thenAccept { texture ->
                        material.setTexture("secondary", texture)

                        val fadeIn = ValueAnimator.ofFloat(0f, 1f).setDuration(500)
                        fadeIn.startDelay = 5000
                        fadeIn.addUpdateListener { animator ->
                            material.setFloat("crossFade",
                                    if (isFadeIn) 1 - animator.animatedFraction else animator.animatedFraction)
                        }
                        fadeIn.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                isFadeIn = !isFadeIn
                                animation.start()
                            }
                        })

                        fadeIn.start()
                    }
                    .exceptionally { throwable ->
                        Log.d(TAG, "Could not load texture path: " + areaVisual.getDetailValue(KEY_IMAGEPATH)!!, throwable)
                        null
                    }
        }
    }

    override
    fun getEventDetails(): SparseArray<EventDetail> {
        return areaVisual.events
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)!!

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.OVERLAY)

        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    companion object {
        private val TAG = ImageNode::class.java.simpleName

        fun builder(context: Context, areaVisual: AreaVisual): ImageNode {
            return ImageNode(context, areaVisual)
        }
    }
}
