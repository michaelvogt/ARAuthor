package eu.michaelvogt.ar.author.locations;


import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.concurrent.CompletableFuture;

import eu.michaelvogt.ar.author.R;

public class Kumagaike {
  private static final String TAG = Kumagaike.class.getSimpleName();

  public static final String SIGN = "kumagaike_sign";

  public static void displayInfoScene(Context context, AnchorNode anchorNode, AugmentedImage image) {
    InfoPanel
        .build(context)
        .thenAccept(infoPanel -> {
          Node panel = new Node();
          panel.setRenderable(infoPanel);
          panel.setLocalPosition(new Vector3(image.getExtentX(), 0, image.getExtentZ()));
          panel.setLocalRotation(Quaternion.axisAngle(new Vector3(1, 0, 0), -90));
          panel.setParent(anchorNode);

          View panelView = infoPanel.getView();
          panelView.findViewById(R.id.button_video).setOnClickListener((event) -> {
            Log.d(TAG, "video pressed");

            CompletableFuture<Texture> textureFuture = Texture.builder()
                .setSource(context, R.drawable.info_text)
                .setUsage(Texture.Usage.DATA)
                .build()
                .exceptionally(throwable -> {
                  Log.e(TAG, "Unable to create texture.", throwable);
                  return null;
                });

            textureFuture.thenAccept(texture -> MaterialFactory.makeOpaqueWithTexture(context, texture)
                .thenAccept(material -> {
                  ModelRenderable panelDisplayRenderable = ShapeFactory.makeCube(
                      new Vector3(image.getExtentX() * 0.637f, .01f, image.getExtentZ() * 0.646f),
                      new Vector3(0.008f, 0, -0.014f), material);

                  Node display = new Node();
                  display.setRenderable(panelDisplayRenderable);
                  display.setParent(anchorNode);
                })
                .exceptionally(throwable -> {
                  Log.e(TAG, "Unable build overlay", throwable);
                  return null;
                }));

          });
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build user panel.", throwable);
          return null;
        });
  }
}
