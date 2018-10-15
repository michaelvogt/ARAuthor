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

package eu.michaelvogt.ar.author.nodes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.EventDetail;
import eu.michaelvogt.ar.author.data.VisualDetailKt;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class ImageNode extends AreaNode implements EventSender {
  private static final String TAG = ImageNode.class.getSimpleName();

  private boolean isFadeIn = true;

  private ImageNode(Context context, AreaVisual areaVisual) {
    super(context, areaVisual);
  }

  public static ImageNode builder(Context context, AreaVisual areaVisual) {
    return new ImageNode(context, areaVisual);
  }

  public ImageNode setIsCameraFacing(boolean isCameraFacing) {
    this.isCameraFacing = isCameraFacing;
    return this;
  }

  public ImageNode setRenderPriority(int renderPriority) {
    this.renderPriority = renderPriority;
    return this;
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    CompletableFuture<Texture> futureTexture;

    if (areaVisual.hasDetail(VisualDetailKt.KEY_IMAGEPATH)) {
      Object detail = areaVisual.getDetail(VisualDetailKt.KEY_IMAGEPATH);
      String textureFilePath = FileUtils.getFullPuplicFolderPath((String) detail);

      futureTexture = Texture.builder()
          .setSource(BitmapFactory.decodeFile(textureFilePath))
          .setUsage(Texture.Usage.COLOR)
          .build()
          .exceptionally(throwable -> {
            Log.d(TAG, "Could not load texture image: " +
                areaVisual.getDetail(VisualDetailKt.KEY_IMAGEPATH), throwable);
            return null;
          });

    } else if (areaVisual.hasDetail(VisualDetailKt.KEY_IMAGERESOURCE)) {
      int textureResource = (int) areaVisual.getDetail(VisualDetailKt.KEY_IMAGERESOURCE, R.drawable.ic_launcher);
      Bitmap bitmap = getBitmapFromVectorDrawable(context, textureResource);

      futureTexture = Texture.builder()
          .setSource(bitmap)
          .setUsage(Texture.Usage.COLOR)
          .build()
          .exceptionally(throwable -> {
            Log.d(TAG, "Could not load texture resource: " +
                areaVisual.getDetail(VisualDetailKt.KEY_IMAGERESOURCE), throwable);
            return null;
          });

    } else {
      throw new IllegalArgumentException("Missing AreaVisual IMAGEPATH or IMAGERESOURCE");
    }

    futureTexture.thenAccept(texture -> ModelRenderable.builder()
        // Keep default model as resource
        .setSource(context, AreaNodeBuilder.CUSTOM_MATERIAL_TEMP)
        .build()
        .thenAccept(temp -> {
          // TODO: Hack - fix when custom material can be created #196
          Material material = temp.getMaterial();
          material.setTexture("primary", texture);
          areaVisual.apply(material);

          setupFadeAnimation(material);

          Renderable renderable = ShapeFactory.makeCube(
              (Vector3) areaVisual.getSize(),
              (Vector3) areaVisual.getZeroPoint(), material);
          areaVisual.apply(renderable);

          // Needs to be set, bacause Sceneform has a layering problem with transparent objects
          // https://github.com/google-ar/sceneform-android-sdk/issues/285#issuecomment-420730274
          renderable.setRenderPriority(renderPriority);

          setRenderable(renderable);

          Log.i(TAG, "ImageNode successfully created");
          future.complete(this);
        })
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not create model " + AreaNodeBuilder.CUSTOM_MATERIAL_TEMP, throwable);
          return null;
        }));

    return future;
  }

  private void setupFadeAnimation(Material material) {
    if (areaVisual.hasDetail(VisualDetailKt.KEY_SECONDARYIMAGEPATH)) {
      String textureFilePath = FileUtils.getFullPuplicFolderPath(
          (String) areaVisual.getDetail(VisualDetailKt.KEY_SECONDARYIMAGEPATH, "Touristar/default/images/"));

      Texture.builder()
          .setSource(BitmapFactory.decodeFile(textureFilePath))
          .setUsage(Texture.Usage.COLOR)
          .build()
          .thenAccept(texture -> {
            material.setTexture("secondary", texture);

            ValueAnimator fadeIn = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
            fadeIn.setStartDelay(5000);
            fadeIn.addUpdateListener(animator -> material.setFloat("crossFade",
                isFadeIn ? 1 - animator.getAnimatedFraction() : animator.getAnimatedFraction()));
            fadeIn.addListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isFadeIn = !isFadeIn;
                animation.start();
              }
            });

            fadeIn.start();
          })
          .exceptionally(throwable -> {
            Log.d(TAG, "Could not load texture path: " +
                areaVisual.getDetail(VisualDetailKt.KEY_IMAGEPATH), throwable);
            return null;
          });
    }
  }

  @Override
  public SparseArray<EventDetail> getEventDetails() {
    return areaVisual.getEvents();
  }

  private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
    Drawable drawable = ContextCompat.getDrawable(context, drawableId);

    assert drawable != null;

    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.OVERLAY);

    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }
}
