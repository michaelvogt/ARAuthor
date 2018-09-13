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

package eu.michaelvogt.ar.author.utils;

import android.content.Context;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.nodes.AreaNode;
import eu.michaelvogt.ar.author.nodes.ComparisonNode;
import eu.michaelvogt.ar.author.nodes.ImageNode;
import eu.michaelvogt.ar.author.nodes.SliderNode;
import eu.michaelvogt.ar.author.nodes.TextNode;

public class AreaNodeBuilder {
  private static final String TAG = AreaNodeBuilder.class.getSimpleName();

  // TODO: Remove when Sceneform supports creation of custom materials #196
  public static final int CUSTOM_MATERIAL_TEMP = R.raw.default_model;
  public static final int SLIDE_MATERIAL_TEMP = R.raw.slide;
  public static final int COMPARISON_MATERIAL_TEMP = R.raw.compare;

  private final Context context;
  private final Area area;
  private Scene scene;

  private AreaNodeBuilder(Context context, Area area) {
    this.context = context;
    this.area = area;
  }

  public static AreaNodeBuilder builder(Context context, Area area) {
    return new AreaNodeBuilder(context, area);
  }

  public AreaNodeBuilder setScene(Scene scene) {
    this.scene = scene;
    return this;
  }

  public CompletionStage<Node> build() {
    switch (area.getObjectType()) {
      case Area.TYPE_DEFAULT:
      case Area.TYPE_3DOBJECTONIMAGE:
        return future3dObjectOnImage();
      case Area.TYPE_3DOBJECTONPLANE:
        return null;
      case Area.TYPE_BACKGROUNDONIMAGE:
        return ImageNode.builder(context, area).setRenderPriority(AreaNode.RENDER_FIRST).build();
      case Area.TYPE_SLIDESONIMAGE:
        return SliderNode.builder(context, area).setScene(scene).build();
      case Area.TYPE_INTERACTIVEOVERLAY:
        return null;
      case Area.TYPE_INTERACTIVEPANEL:
        return null;
      case Area.TYPE_TEXTONIMAGE:
        return TextNode.builder(context, area).build();
      case Area.TYPE_IMAGEONIMAGE:
        return ImageNode.builder(context, area).build();
      case Area.TYPE_ROTATIONBUTTON:
        return ImageNode.builder(context, area).setIsCameraFacing(true).build();
      case Area.TYPE_COMPARATORONIMAGE:
        return ComparisonNode.builder(context, area).build();
      default:
        throw new IllegalArgumentException("Unknown area object type: " + area.getObjectType());
    }
  }

  private CompletionStage<Node> future3dObjectOnImage() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    ModelRenderable.builder()
        .setSource(context, area.getResource())
        .build()
        .thenAccept(renderable -> future.complete(createNode(renderable)));

    return future;
  }

  private CompletionStage<Node> future3dPlaneOverlay() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    return future;
  }

  private Node createNode(Renderable renderable) {
    Node node = new Node();
    node.setRenderable(renderable);
    node.setLocalPosition(area.getPosition());
    node.setLocalRotation(area.getRotation());
    node.setLocalScale(area.getScale());
    return node;
  }
}
