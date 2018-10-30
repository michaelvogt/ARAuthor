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

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.VisualDetailKt;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class ComparisonNode extends AreaNode {
  private static final String TAG = ComparisonNode.class.getSimpleName();

  private ComparisonNode(Context context, AreaVisual areaVisual) {
    super(context, areaVisual);
  }

  public static ComparisonNode builder(Context context, AreaVisual areaVisual) {
    return new ComparisonNode(context, areaVisual);
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    CompletableFuture<Texture> futurePrimeTexture = getTextureFuture(future, VisualDetailKt.KEY_IMAGEPATH);

    // TODO: There can be more than 1 secondary images available in the future.
    CompletableFuture<Texture> futureSecTexture = getTextureFuture(future, VisualDetailKt.KEY_SECONDARYIMAGEPATH);

    CompletableFuture.allOf(futurePrimeTexture, futureSecTexture)
        .thenAccept(aVoid -> ModelRenderable.builder()
            .setSource(context, AreaNode.COMPARISON_MATERIAL_TEMP)
            .build()
            .thenAccept(temp -> {
              // TODO: Hack - fix when custom material can be created #196
              Material material = temp.getMaterial();
              // area.applyDetail(material);

              try {
                material.setTexture("primaryTexture", futurePrimeTexture.get());
                material.setTexture("secondaryTexture", futureSecTexture.get());
              } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
              }

              Renderable renderable = ShapeFactory.makeCube(areaVisual.getSize(),
                  areaVisual.getZeroPoint(), material);
              areaVisual.apply(renderable);
              setRenderable(renderable);

              setOnTouchListener((hitTestResult, motionEvent) -> {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN || motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {
                  Vector3 normalLocation = worldToLocalPoint(hitTestResult.getPoint());
                  material.setFloat("dividerLocation", (float) (normalLocation.x + 0.45));

                  Log.i(TAG, "hit point x: " + hitTestResult.getPoint().x + " local point x: " + normalLocation.x);
                }
                return true;
              });

              Log.i(TAG, "ComparisonNode successfully created");
              future.complete(this);
            })
            .exceptionally(throwable -> {
              Log.d(TAG, "Could not create model " + AreaNode.COMPARISON_MATERIAL_TEMP, throwable);
              future.completeExceptionally(throwable);
              return null;
            }))
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build textures.", throwable);
          return null;
        });

    return future;
  }

  private CompletableFuture<Texture> getTextureFuture(CompletableFuture<Node> future, int detailPath) {
    String fullPath = FileUtils.getFullPuplicFolderPath((String) areaVisual.getDetailValue(detailPath));

    return Texture.builder()
        .setSource(BitmapFactory.decodeFile(fullPath))
        .setUsage(Texture.Usage.COLOR)
        .build()
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not create texture builder for " + areaVisual.getDetailValue(detailPath), throwable);
          future.completeExceptionally(throwable);
          return null;
        });
  }
}
