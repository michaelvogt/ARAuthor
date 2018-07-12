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

package eu.michaelvogt.ar.author.locations.goryoukaku;

import android.content.Context;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.NodeParent;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.lyft.android.scissors.CropView;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.nodes.InfoPanel;
import eu.michaelvogt.ar.author.nodes.InteractiveImageRenderable;

public class Office {
  private static final String TAG = Office.class.getSimpleName();
  private static final float IMAGE_YEXTEND = 0.3f;
  private static final float IMAGE_XEXTEND = 0.45f;

  public static final String SIGN_FRONT = "office_sign_front";
  public static final String SIGN_BACK = "office_sign_back";

  private boolean oldHouseAttached;
  private Anchor oldHouseAnchor;
  private AnchorNode oldHouseAnchorNode;
  private Node oldHouseNode;

  public void displayInfoScene(
      Context context, AnchorNode anchorNode, AugmentedImage arImage, Session session) {
    new InfoPanel()
        .build(context)
        .thenAccept(infoPanel -> {
          float widthOffset = infoPanel.getView().getWidth() * infoPanel.getPixelsToMetersRatio();
          float heightOffset =
              infoPanel.getView().getHeight() * infoPanel.getPixelsToMetersRatio() / 2;

          Node panel = attachNode(anchorNode, infoPanel);
          panel.setLocalPosition(
              new Vector3(IMAGE_XEXTEND + widthOffset, 0.01f, heightOffset));
          panel.setLocalRotation(Quaternion.axisAngle(new Vector3(1, 0, 0), -90));

          infoPanel.getView().findViewById(R.id.button_grab).setOnClickListener(event -> {
            if (!oldHouseAttached) {
              oldHouseAttached = true;

              ModelRenderable.builder()
                  .setSource(context, R.raw.office_full)
                  .build()
                  .thenAccept(oldHouse -> {
                    Pose centerPose = arImage.getCenterPose();
                    oldHouseAnchor = session.createAnchor(centerPose.compose(Pose
                        .makeTranslation(0, 0, 1.5f)).extractTranslation());

                    oldHouseAnchorNode = new AnchorNode(oldHouseAnchor);
                    oldHouseAnchorNode.setParent(anchorNode.getScene());

                    oldHouseNode = new Node();
                    oldHouseNode.setRenderable(oldHouse);
                    oldHouseNode.setLocalPosition(new Vector3(0.0f, -3.0f, -3.0f));
                    oldHouseNode.setParent(oldHouseAnchorNode);
                  })
                  .exceptionally(
                      throwable -> {
                        Log.e(TAG, "Unable to load Renderable.", throwable);
                        return null;
                      });
            } else {
              oldHouseNode.setParent(null);
              oldHouseNode.setRenderable(null);
              oldHouseNode = null;

              oldHouseAnchor.detach();
              oldHouseAnchor = null;
              oldHouseAttached = false;
            }
          });
        });

    new InteractiveImageRenderable()
        .build(context)
        .thenAccept(interactiveImage -> {
          CropView imageView = interactiveImage.getView().findViewById(R.id.image_interactive);
          int resource = arImage.getName().equals(SIGN_FRONT)
              ? R.drawable.office_schema_front : R.drawable.office_schema_back;
          imageView.setImageResource(resource);

          Node image = attachNode(anchorNode, interactiveImage);
          image.setLocalPosition(new Vector3(0.2025f, 0, 0.285f));
          image.setLocalRotation(Quaternion.axisAngle(new Vector3(1, 0, 0), -90));
        });

    MaterialFactory
        .makeTransparentWithColor(context, new Color(1.0f, .5f, .5f, .5f))
        .thenAccept(color -> {
//          attachNode(anchorNode, getSchemaRenderable(color));
          attachNode(anchorNode, getTextRenderable(color));
          attachNode(anchorNode, getMapRenderable(color));
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build overlay", throwable);
          return null;
        });
  }

  private Node attachNode(NodeParent parent, Renderable renderable) {
    Node node = new Node();
    node.setRenderable(renderable);
    node.setParent(parent);
    return node;
  }

  private Renderable getSchemaRenderable(Material color) {
    return ShapeFactory.makeCube(
        new Vector3(0.415f, .0001f, 0.54f),
        new Vector3(0.2025f, 0, 0.02f), color);
  }

  private Renderable getTextRenderable(Material color) {
    return ShapeFactory.makeCube(
        new Vector3(0.42f, 0.0001f, 0.24f),
        new Vector3(-0.22f, 0, -0.10f), color);
  }

  private Renderable getMapRenderable(Material color) {
    return ShapeFactory.makeCube(
        new Vector3(0.247f, .001f, 0.247f),
        new Vector3(-0.2125f, 0, 0.1565f), color);

  }
}
