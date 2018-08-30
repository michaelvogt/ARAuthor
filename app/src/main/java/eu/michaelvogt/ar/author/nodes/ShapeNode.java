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

import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.utils.Detail;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class ShapeNode extends Node {
  private static final String TAG = ShapeNode.class.getSimpleName();

  private Context context;
  private final Area area;

  private ShapeNode(Context context, Area area) {
    this.context = context;
    this.area = area;

    setLocalPosition(area.getPosition());
    setLocalRotation(area.getRotation());
    setLocalScale(area.getScale());
  }

  public static ShapeNode builder(Context context, Area area) {
    return new ShapeNode(context, area);
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    String textureFilePath = FileUtils.getFullPuplicFolderPath(
        area.getDetailString(Detail.IMAGEPATH, "Touristar/default/images/"));

    Texture.builder()
        .setSource(BitmapFactory.decodeFile(textureFilePath))
        .setUsage(Texture.Usage.COLOR)
        .build()
        .thenAccept(texture -> MaterialFactory.makeTransparentWithTexture(context, texture)
            // Keep default model as resource
            .thenAccept(material -> {
              area.applyDetail(material);

              Renderable renderable = ShapeFactory.makeCube(area.getSize(), Vector3.zero(), material);
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
              area.getDetail(Detail.IMAGEPATH, "Touristar/default/images/"), throwable);
          return null;
        });

    return future;
  }


}