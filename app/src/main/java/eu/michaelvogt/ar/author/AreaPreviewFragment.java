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
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
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
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AreaPreviewFragment extends Fragment {
  private static final String TAG = AreaPreviewFragment.class.getSimpleName();
  private final Color PREVIEWCOLOR = new Color(0.7f, 0.45f, 0.15f, 0.5f);

  private int areaIndex;
  private Marker editMarker;
  private Area editArea;

  private LoopArFragment arFragment;
  private AugmentedImage augmentedImage;
  private View editButton;
  private boolean useTranslucency;

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
    int markerId = getArguments().getInt("marker_id");
    editMarker = viewModel.getMarker(markerId);

    areaIndex = getArguments().getInt("area_id");
    editArea = viewModel.getArea(editMarker.getAreaId(areaIndex));

    arFragment = (LoopArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
    arFragment.getPlaneDiscoveryController().hide();
    arFragment.getPlaneDiscoveryController().setInstructionView(null);

    useTranslucency = getArguments().getInt("area_edit_translucency") == 1;

    // TODO: Currently all markers are imported. Restrict to the currently edited marker

    editButton = view.findViewById(R.id.area_edit);
    editButton.setOnClickListener(event -> {
      Bundle bundle = new Bundle();
      bundle.putInt("marker_id", markerId);
      bundle.putInt("area_id", areaIndex);
      bundle.putInt("area_edit_translucency", useTranslucency ? 1 : 0);
      Navigation.findNavController(view).navigate(R.id.action_edit_area, bundle);
    });

    arFragment.getArSceneView().getScene().setOnUpdateListener(this::onUpdateFrame);
  }

  private void onUpdateFrame(FrameTime frameTime) {
    arFragment.onUpdate(frameTime);

    Frame frame = arFragment.getArSceneView().getArFrame();

    if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
      return;
    }

    Collection<AugmentedImage> updatedArImages = frame.getUpdatedTrackables(AugmentedImage.class);

    for (AugmentedImage image : updatedArImages) {
      if (image.getName().equals(editMarker.getTitle())) {
        switch (image.getTrackingState()) {
          case TRACKING:
            if (augmentedImage == null) {
              Anchor anchor = image.createAnchor(image.getCenterPose());
              AnchorNode anchorNode = new AnchorNode(anchor);
              anchorNode.setParent(arFragment.getArSceneView().getScene());

              Vector3 size = editArea.getSize();
              Vector3 location = editArea.getPosition();

              Session session = arFragment.getArSceneView().getSession();

              switch (editArea.getObjectType()) {
                case Area.TYPE_3DOBJECT:
                  buildModelRenderable(getContext(), editArea.getResource(),
                      image, editArea.getPosition(), editArea.getRotation(), anchorNode, session);
                  break;
                default:
                  buildShapeRenderable(getContext(), anchorNode);
              }

              editButton.setVisibility(View.VISIBLE);
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

  private void buildShapeRenderable(Context context, AnchorNode imageAnchor) {
    createMaterial(context)
        .thenAccept(material -> {
          ModelRenderable shape = ShapeFactory.makeCube(editArea.getSize(), Vector3.zero(), material);
          attachRenderable(shape, imageAnchor);
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build ShapeRenderable.", throwable);
          return null;
        });
  }

  private void buildModelRenderable(Context context, int resourceId,
                                    AugmentedImage image, Vector3 position,
                                    Quaternion rotation, AnchorNode imageAnchor, Session session) {
    ModelRenderable.builder()
        // TODO: Load model from external position

        .setSource(context, resourceId)
        .build()
        .thenAccept(modelRenderable -> {
          createMaterial(context)
              .thenAccept(modelRenderable::setMaterial);

          Pose modelPose = image.getCenterPose();
          float[] modelTranslation = modelPose.extractTranslation()
              .transformPoint(new float[]{position.x, position.y, position.z});
          float[] modelRotation = modelPose.extractRotation().getRotationQuaternion();
          Anchor anchor = session.createAnchor(
              new Pose(modelTranslation, new float[]{0f, modelRotation[1], 0f, modelRotation[3]}));

          AnchorNode anchorNode = new AnchorNode(anchor);
          anchorNode.setLocalScale(editArea.getScale());
          anchorNode.setParent(arFragment.getArSceneView().getScene());

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

  private CompletableFuture<Material> createMaterial(Context context) {
    return useTranslucency ?
        MaterialFactory.makeTransparentWithColor(context, PREVIEWCOLOR) :
        MaterialFactory.makeOpaqueWithColor(context, PREVIEWCOLOR);
  }

  private void attachRenderable(Renderable renderable, AnchorNode anchorNode) {
    Node areaNode = new Node();
    areaNode.setRenderable(renderable);
    areaNode.setLocalPosition(editArea.getPosition());
    areaNode.setLocalRotation(editArea.getRotation());
    areaNode.setLocalScale(editArea.getScale());
    areaNode.setParent(anchorNode);
  }
}
