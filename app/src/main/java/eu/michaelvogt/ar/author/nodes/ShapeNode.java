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

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.VisualDetailKt;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class ShapeNode extends Node {
  private static final String TAG = ShapeNode.class.getSimpleName();

  private Context context;
  private final AreaVisual areaVisual;

  private ShapeNode(Context context, AreaVisual areaVisual) {
    this.context = context;
    this.areaVisual = areaVisual;

    setLocalPosition(areaVisual.getPosition());
    setLocalRotation(areaVisual.getRotation());
    setLocalScale(areaVisual.getScale());
  }

  public static ShapeNode builder(Context context, AreaVisual area) {
    return new ShapeNode(context, area);
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    String textureFilePath = FileUtils.getFullPuplicFolderPath(
        (String) areaVisual.getDetailValue(VisualDetailKt.KEY_IMAGEPATH, "Touristar/default/images/"));

    Texture.builder()
        .setSource(BitmapFactory.decodeFile(textureFilePath))
        .setUsage(Texture.Usage.COLOR)
        .build()
        .thenAccept(texture -> MaterialFactory.makeTransparentWithTexture(context, texture)
            // Keep default model as resource
            .thenAccept(material -> {
              areaVisual.apply(material);

              Renderable renderable = ShapeFactory.makeCube(areaVisual.getSize(), Vector3.zero(), material);
              renderable.setShadowCaster(false);
              setRenderable(renderable);

              Log.i(TAG, "ShapeNode successfully created");
              future.complete(this);
            })
            .exceptionally(throwable -> {
              Log.d(TAG, "Could not create Material", throwable);
              return null;
            }))
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not load texture " +
              areaVisual.getDetailValue(VisualDetailKt.KEY_IMAGEPATH), throwable);
          return null;
        });

    return future;
  }


}
