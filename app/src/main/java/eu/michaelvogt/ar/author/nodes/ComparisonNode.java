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
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.Detail;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class ComparisonNode extends AreaNode {
  private static final String TAG = ComparisonNode.class.getSimpleName();

  private ComparisonNode(Context context, Area area) {
    super(context, area);
  }

  public static ComparisonNode builder(Context context, Area area) {
    return new ComparisonNode(context, area);
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    CompletableFuture<Texture> futurePrim3Texture = getTextureFuture(future, Detail.KEY_IMAGEPATH);

    // TODO: There can be more than 1 secondary images available in the future.
    CompletableFuture<Texture> futureSecTexture = getTextureFuture(future, Detail.KEY_SECONDARYIMAGEPATH);

    CompletableFuture.allOf(futurePrim3Texture, futureSecTexture)
        .thenAccept(aVoid -> ModelRenderable.builder()
        .setSource(context, AreaNodeBuilder.COMPARISON_MATERIAL_TEMP)
        .build()
        .thenAccept(temp -> {
          // TODO: Hack - fix when custom material can be created #196
          Material material = temp.getMaterial();
              // area.applyDetail(material);

          try {
            material.setTexture("primaryTexture", futurePrim3Texture.get());
            material.setTexture("secondaryTexture", futureSecTexture.get());
          } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
          }

          Renderable renderable = ShapeFactory.makeCube(area.getSize(), area.getZeroPoint(), material);
          area.applyDetail(renderable);
          setRenderable(renderable);

          setOnTouchListener((hitTestResult, motionEvent) -> {
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN || motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE ) {
              float normalLocation = (float) (Math.max(0.01, hitTestResult.getPoint().x + 0.5));
              material.setFloat("dividerLocation", normalLocation);

              Log.i(TAG, "ray point x: " + hitTestResult.getPoint().x);
            }
            return true;
          });

          Log.i(TAG, "ComparisonNode successfully created");
          future.complete(this);
        })
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not create model " + AreaNodeBuilder.COMPARISON_MATERIAL_TEMP, throwable);
              future.completeExceptionally(throwable);
          return null;
        }));

    return future;
  }

  private CompletableFuture<Texture> getTextureFuture(CompletableFuture<Node> future, int detailPath) {
    String fullPath = FileUtils.getFullPuplicFolderPath(area.getDetailString(detailPath));

    return Texture.builder()
        .setSource(BitmapFactory.decodeFile(fullPath))
        .setUsage(Texture.Usage.COLOR)
        .build()
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not create texture builder for " + area.getDetail(detailPath), throwable);
          future.completeExceptionally(throwable);
          return null;
        });
  }
}
