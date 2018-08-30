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
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Plane;
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
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.lyft.android.scissors.CropView;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import eu.michaelvogt.ar.author.MarkerPreviewFragment;
import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.nodes.InfoPanel;
import eu.michaelvogt.ar.author.nodes.InteractiveImageRenderable;

public class Office {
  private static final String TAG = Office.class.getSimpleName();

  private static final Color NOPLANECOLOR = new Color(1.0f, 0.0f, 0.0f, 1f);
  private static final Color PLANECOLOR = new Color(0.0f, 1.0f, 0.0f, 1f);
  private static final Color PREVIEWCOLOR = new Color(0.337f, 0.227f, 0.114f, 1f);

  public static final String SIGN_FRONT = "office_sign_front";
  public static final String SIGN_BACK = "office_sign_back";

  private boolean officeAttached;
  private Anchor officeAnchor;
  private Node officeNode;

  private AuthorViewModel viewModel;
  private MarkerPreviewFragment.SetModelPoseOnPlaneListener setOnPlaneListener;

  private boolean isLanguageToggled = false;

  public void displayInfoScene(Context context, AuthorViewModel viewModel, ArFragment arFragment,
                               AnchorNode anchorNode, AugmentedImage arImage, Session session,
                               MarkerPreviewFragment.SetModelPoseOnPlaneListener setOnPlaneListener) {
    this.viewModel = viewModel;
    this.setOnPlaneListener = setOnPlaneListener;

    // TODO: Provide Marker or markerId from marker preview
    if (viewModel.getMarker(3).isShowBackground()) {
      Area backgroundArea = new Area(
          Area.TYPE_VIEWONIMAGE,
          "Magistrates Office Background",
          R.drawable.background,
          new Vector3(0.9f, 0.001f, 0.6f),
          Area.COORDINATE_LOCAL,
          Vector3.zero(),
          new Quaternion(),
          Vector3.one());

      CompletableFuture<Texture> textureFuture = Texture.builder()
          .setSource(context, backgroundArea.getResource())
          .setUsage(Texture.Usage.COLOR)
          .build()
          .exceptionally(throwable -> {
            Log.e(TAG, "Unable to create texture.", throwable);
            return null;
          });

      textureFuture.thenAccept(texture -> MaterialFactory.makeTransparentWithTexture(context, texture)
          .thenAccept(material -> {
                Node backgroundNode = attachNode(anchorNode, getTextureRenderable(material, backgroundArea), backgroundArea);
                attachAreas(context, viewModel, arFragment, anchorNode, arImage, session, backgroundNode);
              }
          ));
    } else {
      attachAreas(context, viewModel, arFragment,
          anchorNode, arImage, session, anchorNode);
    }
  }

  private void attachAreas(Context context, AuthorViewModel viewModel, ArFragment arFragment, AnchorNode anchorNode, AugmentedImage arImage, Session session, Node backgroundNode) {
    attachInfoPanel(context, viewModel, arFragment, anchorNode, arImage, session);
    attachInteractiveSchema(context, backgroundNode, arImage);
    attachTexturedObject(context, backgroundNode, viewModel.getArea(3));
    attachTexturedLanguageToggleObject(context, backgroundNode, viewModel.getArea(2));
  }

  private void attachInfoPanel(Context context, AuthorViewModel viewModel, ArFragment arFragment,
                               Node anchorNode, AugmentedImage arImage, Session session) {
    new InfoPanel()
        .build(context)
        .thenAccept(infoPanel -> {
          attachNode(anchorNode, infoPanel, viewModel.getArea(5));

          infoPanel.getView().findViewById(R.id.button_grab).setOnClickListener(event -> {
            if (!officeAttached) {
              officeAttached = true;

              Area officeArea = viewModel.getArea(0);
              attachPlaneStatusRenderable(context, officeArea, arImage, arFragment, session);
            } else {
              if (officeNode != null) {
                officeNode.setParent(null);
                officeNode.setRenderable(null);
                officeNode = null;

                officeAnchor.detach();
                officeAnchor = null;
                officeAttached = false;
              }
            }
          });
        });
  }

  private void attachTexturedObject(Context context, Node anchorNode, Area area) {
    getTextureFuture(context, area.getResource())
        .thenAccept(texture -> MaterialFactory.makeOpaqueWithTexture(context, texture)
            .thenAccept(material -> attachNode(anchorNode, getTextureRenderable(material, area), area)));
  }

  private void attachTexturedLanguageToggleObject(Context context, Node anchorNode, Area area) {
    getTextureFuture(context, area.getResource()).
        thenAccept(texture -> MaterialFactory.makeOpaqueWithTexture(context, texture)
            .thenAccept(material -> {
              Renderable renderable = getTextureRenderable(material, area);
              Node node = attachNode(anchorNode, renderable, area);
              node.setOnTapListener((hitTestResult, motionEvent) -> {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                  isLanguageToggled = !isLanguageToggled;
                  Texture.builder()
                      .setSource(context, isLanguageToggled
                          ? R.drawable.magistrates_office_en : R.drawable.magistrates_office_jp)
                      .build()
                      .thenAccept(toggleTexture -> {
                        renderable.getMaterial().setTexture(MaterialFactory.MATERIAL_TEXTURE, toggleTexture);
                      })
                      .exceptionally(throwable -> {
                        Log.e(TAG, "Unable to create texture.", throwable);
                        return null;
                      });
                }
              });
            }));
  }

  private CompletableFuture<Texture> getTextureFuture(Context context, int resource) {
    return Texture.builder()
        .setSource(context, resource)
        .build()
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to create texture.", throwable);
          return null;
        });
  }


  private void attachInteractiveSchema(Context context, Node anchorNode, AugmentedImage arImage) {
    new InteractiveImageRenderable()
        .build(context)
        .thenAccept(interactiveImage -> {
          CropView imageView = interactiveImage.getView().findViewById(R.id.image_interactive);
          int resource = arImage.getName().equals(SIGN_FRONT)
              ? R.drawable.office_schema_front : R.drawable.office_schema_back;
          imageView.setImageResource(resource);

          attachNode(anchorNode, interactiveImage, viewModel.getArea(1));
        });
  }

  private void attachPlaneStatusRenderable(Context context, Area officeArea, AugmentedImage image,
                                           ArFragment arFragment, Session session) {

    MaterialFactory.makeOpaqueWithColor(context, NOPLANECOLOR)
        .thenAccept(noPlaneMaterial -> {
          Pose centerPose = image.getCenterPose();
          Vector3 position = officeArea.getPosition();

          float[] mTranslation = centerPose.extractTranslation()
              .transformPoint(new float[]{position.x, position.y, position.z});
          float[] mRotation = centerPose.extractRotation().getRotationQuaternion();
          Pose modelPose = new Pose(mTranslation, new float[]{0f, mRotation[1], 0f, mRotation[3]});

          ModelRenderable shape = ShapeFactory.makeSphere(
              0.3f, new Vector3(modelPose.tx(), modelPose.ty(), modelPose.tz()), noPlaneMaterial);

          Anchor anchor = session.createAnchor(modelPose);

          AnchorNode anchorNode = new AnchorNode(anchor);
          anchorNode.setParent(arFragment.getArSceneView().getScene());

          Node statusNode = new Node();
          statusNode.setRenderable(shape);
          statusNode.setParent(anchorNode);

          setOnPlaneListener.setPlaneListener(() -> {
            Collection<Plane> planes =
                arFragment.getArSceneView().getSession().getAllTrackables(Plane.class);
            for (Plane plane : planes) {
              if (plane.isPoseInExtents(modelPose)) {
                MaterialFactory
                    .makeOpaqueWithColor(context, PLANECOLOR)
                    .thenAccept(shape::setMaterial);

                statusNode.setOnTapListener((hitTestResult, motionEvent) -> {
                  anchorNode.removeChild(statusNode);
                  attachModelRenderable(context, anchorNode, officeArea);
                });
                return true;
              }
            }
            return false;
          });
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build Model plane status object.", throwable);
          return null;
        });
  }

  private void attachModelRenderable(Context context, AnchorNode anchorNode, Area officeArea) {
    ModelRenderable.builder()
        // TODO: Load model from external position

        .setSource(context, officeArea.getResource())
        .build()
        .thenAccept(modelRenderable -> {
          MaterialFactory.makeOpaqueWithColor(context, PREVIEWCOLOR)
              .thenAccept(modelRenderable::setMaterial);

          // Scale was ignored when set on Node
          anchorNode.setLocalScale(officeArea.getScale());

          officeNode = new Node();
          officeNode.setRenderable(modelRenderable);
          officeNode.setLocalRotation(officeArea.getRotation());
          officeNode.setParent(anchorNode);
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build ModelRenderable.", throwable);
          return null;
        });
  }


  private Node attachNode(NodeParent parent, Renderable renderable, Area area) {
    Node node = new Node();
    node.setRenderable(renderable);
    node.setLocalPosition(area.getPosition());
    node.setLocalRotation(area.getRotation());
    node.setLocalScale(area.getScale());
    node.setParent(parent);

    return node;
  }

  private Renderable getTextureRenderable(Material material, Area area) {
    return ShapeFactory.makeCube(
        new Vector3(area.getSize()), Vector3.zero(), material);
  }
}
