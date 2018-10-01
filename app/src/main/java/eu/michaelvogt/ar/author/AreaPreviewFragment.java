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

package eu.michaelvogt.ar.author;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AreaPreviewFragment extends Fragment {
  private static final String TAG = AreaPreviewFragment.class.getSimpleName();

  private static final Color PREVIEWCOLOR = new Color(0.7f, 0.45f, 0.15f, 0.5f);
  private static final Color NOPLANECOLOR = new Color(1.0f, 0.0f, 0.0f, 1f);
  private static final Color PLANECOLOR = new Color(0.0f, 1.0f, 0.0f, 1f);

  private Marker editMarker;
  private Area editArea;

  private LoopArFragment arFragment;
  private AugmentedImage augmentedImage;
  private boolean useTranslucency;

  private List<ModelPoseOnPlaneListener> modelPoseOnPlaneListeners = new ArrayList<>();
  private Node statusMarkerNode;

  public AreaPreviewFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_areapreview, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    AuthorViewModel viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
    // TODO: Make sure propper db uid is provided
    int markerId = getArguments().getInt("marker_id");
    viewModel.getMarker(markerId).observe(this, marker -> editMarker = marker);

    int areaId = getArguments().getInt("area_id");
    viewModel.getArea(areaId).observe(this, area -> editArea = area);

    arFragment = (LoopArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
    arFragment.getPlaneDiscoveryController().hide();
    arFragment.getPlaneDiscoveryController().setInstructionView(null);

    useTranslucency = getArguments().getInt("area_edit_translucency") == 1;

    // TODO: Currently all markers are imported. Restrict to the currently edited marker

    View editButton = view.findViewById(R.id.area_edit);
    editButton.setOnClickListener(event -> {
      Bundle bundle = new Bundle();
      bundle.putInt("marker_id", markerId);
      bundle.putInt("area_id", areaId);
      bundle.putInt("area_edit_translucency", useTranslucency ? 1 : 0);
      Navigation.findNavController(view).navigate(R.id.action_edit_area, bundle);
    });

    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
  }

  private void onUpdateFrame(FrameTime frameTime) {
    arFragment.onUpdate(frameTime);

    Frame frame = arFragment.getArSceneView().getArFrame();
    if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
      return;
    }

    if (modelPoseOnPlaneListeners.size() != 0) {
      for (ModelPoseOnPlaneListener listener : modelPoseOnPlaneListeners) {
        if (listener.onPlane()) {
          modelPoseOnPlaneListeners.remove(listener);
        }
      };
    }

    Collection<AugmentedImage> updatedArImages = frame.getUpdatedTrackables(AugmentedImage.class);
    for (AugmentedImage image : updatedArImages) {
      if (editMarker != null && image.getName().equals(editMarker.getTitle())) {
        switch (image.getTrackingState()) {
          case TRACKING:
            if (augmentedImage == null) {
              Anchor anchor = image.createAnchor(image.getCenterPose());
              AnchorNode anchorNode = new AnchorNode(anchor);
              anchorNode.setParent(arFragment.getArSceneView().getScene());

              Session session = arFragment.getArSceneView().getSession();

              switch (editArea.getObjectType()) {
                case Area.TYPE_3DOBJECTONPLANE:
                  attachPlaneStatusRenderable(getContext(),
                      image, editArea.getPosition(), editArea.getRotation(), session);
                  break;
                default:
                  attachShapeRenderable(getContext(), anchorNode);
              }

              augmentedImage = image;
            }
            break;

          case STOPPED:
            augmentedImage = null;
            break;
        }
      }
    }
  }

  private void attachShapeRenderable(Context context, AnchorNode imageAnchor) {
    createMaterial(context, PREVIEWCOLOR, useTranslucency)
        .thenAccept(material -> {
          ModelRenderable shape = ShapeFactory.makeCube(editArea.getSize(), Vector3.zero(), material);
          attachRenderable(shape, imageAnchor);
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build ShapeRenderable.", throwable);
          return null;
        });
  }

  private void attachModelRenderable(Context context, AnchorNode anchorNode, Quaternion rotation) {
    if (statusMarkerNode != null) {
      anchorNode.removeChild(statusMarkerNode);
      statusMarkerNode = null;
    }

    ModelRenderable.builder()
        // TODO: Load model from external position

        .setSource(context, editArea.getResource())
        .build()
        .thenAccept(modelRenderable -> {
          createMaterial(context, PREVIEWCOLOR, useTranslucency)
              .thenAccept(modelRenderable::setMaterial);

          anchorNode.setLocalScale(editArea.getScale());

          Node areaNode = new Node();
          areaNode.setRenderable(modelRenderable);
          areaNode.setLocalRotation(rotation);
          areaNode.setParent(anchorNode);
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build ModelRenderable.", throwable);
          return null;
        });
  }

  private void attachPlaneStatusRenderable(Context context, AugmentedImage image, Vector3 position,
                                           Quaternion rotation, Session session) {

    createMaterial(context, NOPLANECOLOR, false)
        .thenAccept(material -> {
          Pose centerPose = image.getCenterPose();
          float[] mTranslation = centerPose.extractTranslation()
              .transformPoint(new float[]{position.x, position.y, position.z});
          float[] mRotation = centerPose.extractRotation().getRotationQuaternion();
          Pose modelPose = new Pose(mTranslation, new float[]{0f, mRotation[1], 0f, mRotation[3]});

          ModelRenderable shape = ShapeFactory.makeSphere(
              0.05f, new Vector3(modelPose.tx(), modelPose.ty(), modelPose.tz()), material);

          Anchor anchor = session.createAnchor(modelPose);

          AnchorNode anchorNode = new AnchorNode(anchor);
          anchorNode.setParent(arFragment.getArSceneView().getScene());

          statusMarkerNode = new Node();
          statusMarkerNode.setRenderable(shape);
          statusMarkerNode.setParent(anchorNode);

          modelPoseOnPlaneListeners.add(() -> {
            Collection<Plane> planes =
                arFragment.getArSceneView().getSession().getAllTrackables(Plane.class);
            for(Plane plane : planes) {
              if (plane.isPoseInExtents(modelPose)) {
                attachModelRenderable(context, anchorNode, rotation);
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

  private CompletableFuture<Material> createMaterial(
      Context context, Color color, boolean isTranslucent) {
    return isTranslucent ?
        MaterialFactory.makeTransparentWithColor(context, color) :
        MaterialFactory.makeOpaqueWithColor(context, color);
  }

  private void attachRenderable(Renderable renderable, AnchorNode anchorNode) {
    Node areaNode = new Node();
    areaNode.setRenderable(renderable);
    areaNode.setLocalPosition(editArea.getPosition());
    areaNode.setLocalRotation(editArea.getRotation());
    areaNode.setLocalScale(editArea.getScale());
    areaNode.setParent(anchorNode);
  }

  private interface ModelPoseOnPlaneListener {
    boolean onPlane();
  }
}
