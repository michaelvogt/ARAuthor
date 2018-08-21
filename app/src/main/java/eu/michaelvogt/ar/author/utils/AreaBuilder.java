package eu.michaelvogt.ar.author.utils;

import android.content.Context;
import android.util.Log;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.AuthorActivity;
import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;

public class AreaBuilder {
  private static final String TAG = AreaBuilder.class.getSimpleName();
  // TODO: Remove when Sceneform supports creation of custom materials #196
  private static final int CUSTOM_MATERIAL_TEMP = R.raw.monarch;

  private final Context context;
  private final Area area;

  private AreaBuilder(Context context, Area area) {
    this.context = context;
    this.area = area;
  }

  public static AreaBuilder builder(Context context, Area area) {
    return new AreaBuilder(context, area);
  }

  public CompletionStage<Node> build() {
    switch (area.getObjectType()) {
      case Area.TYPE_DEFAULT:
      case Area.TYPE_3DOBJECTONIMAGE:
        return future3dObjectOnImage();
      case Area.TYPE_3DOBJECTONPLANE:
        return null;
      case Area.TYPE_VIEWONIMAGE:
        return futureViewOnImage();
      case Area.TYPE_INTERACTIVEOVERLAY:
        return null;
      case Area.TYPE_INTERACTIVEPANEL:
        return null;
      case Area.TYPE_INTERNATIONALTEXT:
        return null;
      case Area.TYPE_TEXTUREDPLANE:
        return futureTexturedPlane();
      default:
        // TODO: Throw ecxeption, getting handled in exceptionally. How to ...?
        return null;
    }
  }

  private CompletionStage<Node> future3dObjectOnImage() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    ((AuthorActivity) context).runOnUiThread(() -> ModelRenderable.builder()
        .setSource(context, area.getResource())
        .build()
        .thenAccept(renderable -> future.complete(createNode(renderable))));

    return future;
  }

  private CompletionStage<Node> future3dPlaneOverlay() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    ((AuthorActivity) context).runOnUiThread(() -> {

    });

    return future;
  }

  private CompletionStage<Node> futureViewOnImage() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    ((AuthorActivity) context).runOnUiThread(() -> ViewRenderable.builder()
        .setView(context, area.getResource())
        .build()
        .thenAccept(renderable -> future.complete(createNode(renderable))));

    return future;
  }

  private CompletionStage<Node> futureTexturedPlane() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    ((AuthorActivity) context).runOnUiThread(() -> {
      ModelRenderable.builder()
          // TODO: Load model from external location
          .setSource(context, CUSTOM_MATERIAL_TEMP)
          .build()
          .thenAccept(temp -> {
            Material material = temp.getMaterial();
            material.setFloat("materialParams.alphaFactor", 0.5f);
            ModelRenderable renderable = ShapeFactory.makeCube(area.getSize(), Vector3.zero(), material);
            future.complete(createNode(renderable));
          });
    });

    return future;
  }

  private Node createNode(Renderable renderable) {
    Node node = new Node();
    node.setRenderable(renderable);
    node.setLocalPosition(area.getPosition());
    node.setLocalRotation(area.getRotation());
    return node;
  }
}
