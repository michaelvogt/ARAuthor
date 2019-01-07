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

package eu.michaelvogt.ar.author.nodes;

import android.content.Context;
import android.util.Log;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.AreaVisualKt;
import eu.michaelvogt.ar.author.data.VisualDetailKt;

public class AreaNodeBuilder {
  private static final String TAG = AreaNodeBuilder.class.getSimpleName();

  private final Context context;
  private final AreaVisual areaVisual;
  private Scene scene;

  private AreaNodeBuilder(Context context, AreaVisual areaVisual) {
    this.context = context;
    this.areaVisual = areaVisual;
  }

  public static AreaNodeBuilder builder(Context context, AreaVisual areaVisual) {
    return new AreaNodeBuilder(context, areaVisual);
  }

  public AreaNodeBuilder setScene(Scene scene) {
    this.scene = scene;
    return this;
  }

  public CompletionStage<Node> build() {
    switch (areaVisual.getObjectType()) {
      case AreaVisualKt.TYPE_DEFAULT:
      case AreaVisualKt.TYPE_3DOBJECTONIMAGE:
        return future3dObjectOnImage();
      case AreaVisualKt.TYPE_3DOBJECTONPLANE:
        return null;
      case AreaVisualKt.TYPE_BACKGROUNDONIMAGE:
        return ImageNode.Companion.builder(context, areaVisual).setRenderPriority(AreaNode.RENDER_FIRST).build();
      case AreaVisualKt.TYPE_SLIDESONIMAGE:
        return SliderNode.builder(context, areaVisual).setScene(scene).build();
      case AreaVisualKt.TYPE_INTERACTIVEOVERLAY:
        return null;
      case AreaVisualKt.TYPE_INTERACTIVEPANEL:
        return null;
      case AreaVisualKt.TYPE_TEXTONIMAGE:
        return TextNode.builder(context, areaVisual).setRenderPriority(5).build();
      case AreaVisualKt.TYPE_IMAGEONIMAGE:
        return ImageNode.Companion.builder(context, areaVisual).build();
      case AreaVisualKt.TYPE_ROTATIONBUTTON:
        return ImageNode.Companion.builder(context, areaVisual).setIsCameraFacing(true).build();
      case AreaVisualKt.TYPE_COMPARATORONIMAGE:
        return ComparisonNode.builder(context, areaVisual).build();
      default:
        throw new IllegalArgumentException("Unknown area object type: " + areaVisual.getObjectType());
    }
  }

  private CompletionStage<Node> future3dObjectOnImage() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    ModelRenderable.builder()
        .setSource(context, (Integer) areaVisual.getDetailValue(VisualDetailKt.KEY_RESOURCE))
        .build()
        .thenAccept(renderable -> future.complete(createNode(renderable)))
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build ModelRenderable.", throwable);
          return null;
        });

    return future;
  }

  private CompletionStage<Node> future3dPlaneOverlay() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    return future;
  }

  private Node createNode(Renderable renderable) {
    Node node = new Node();
    node.setRenderable(renderable);
    node.setLocalPosition(areaVisual.getPosition());
    node.setLocalRotation(areaVisual.getRotation());
    node.setLocalScale(areaVisual.getScale());
    return node;
  }
}
