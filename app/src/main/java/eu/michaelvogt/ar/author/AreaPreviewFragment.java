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
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Collection;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AreaPreviewFragment extends Fragment {
  private static final String TAG = AreaPreviewFragment.class.getSimpleName();
  private final Color TRANSPARENT_COLOR = new Color(0.7f, 0.45f, 0.15f, 0.5f);

  private int areaIndex;
  private Marker editMarker;
  private Area editArea;
  private AuthorViewModel viewModel;

  private LoopArFragment arFragment;
  private AugmentedImage augmentedImage;
  private TransformableNode areaNode;
  private View editButton;

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
    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
    int markerId = getArguments().getInt("markerid");
    editMarker = viewModel.getMarker(markerId);

    areaIndex = getArguments().getInt("areaid");
    String areaId = editMarker.getAreaId(areaIndex);
    editArea = viewModel.getArea(areaId).get();

    arFragment = (LoopArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
    arFragment.getPlaneDiscoveryController().hide();
    arFragment.getPlaneDiscoveryController().setInstructionView(null);

    // TODO: Currently all markers are imported. Restrict to the currently edited marker

    editButton = view.findViewById(R.id.area_edit);
    editButton.setOnClickListener(event -> {
      Vector3 position;
      Quaternion rotation;

      if (editArea.getCoordType() == Area.COORDINATE_LOCAL) {
        position = areaNode.getLocalPosition();
        rotation = areaNode.getLocalRotation();
      } else {
        position = areaNode.getWorldPosition();
        rotation = areaNode.getWorldRotation();
      }

      Bundle bundle = new Bundle();
      bundle.putInt("markerid", markerId);
      bundle.putInt("areaid", areaIndex);

      bundle.putFloat("loc_x", position.x);
      bundle.putFloat("loc_y", position.y);
      bundle.putFloat("loc_z", position.z);
      bundle.putFloat("rot_x", rotation.x);
      bundle.putFloat("rot_y", rotation.y);
      bundle.putFloat("rot_z", rotation.z);
      bundle.putFloat("rot_w", rotation.w);
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
                  buildShapeRenderable(getContext(),
                      image, anchorNode, session);
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

  private void buildShapeRenderable(Context context, AugmentedImage image, AnchorNode imageAnchor, Session session) {
    MaterialFactory
        .makeTransparentWithColor(context, TRANSPARENT_COLOR)
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
                                    AugmentedImage image, Vector3 location,
                                    Quaternion rotation, AnchorNode imageAnchor, Session session) {
    ModelRenderable.builder()
        // TODO: Load model from external location

        .setSource(context, resourceId)
        .build()
        .thenAccept(modelRenderable -> {
          MaterialFactory
              .makeTransparentWithColor(context, TRANSPARENT_COLOR)
              .thenAccept(modelRenderable::setMaterial);

          modelRenderable.setShadowCaster(true);

          Anchor anchor = session.createAnchor(new Pose(
              new float[]{location.x, location.y, location.z},
              new float[]{rotation.x, rotation.y, rotation.z, rotation.w}));

          AnchorNode anchorNode = new AnchorNode(anchor);
          anchorNode.setParent(imageAnchor.getScene());

          areaNode = new TransformableNode(arFragment.getTransformationSystem());
          areaNode.setRenderable(modelRenderable);
          areaNode.setParent(anchorNode);
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build ModelRenderable.", throwable);
          return null;
        });
  }

  private void attachRenderable(Renderable renderable, AnchorNode anchorNode) {
    areaNode = new TransformableNode(arFragment.getTransformationSystem());
    areaNode.setRenderable(renderable);
    areaNode.setLocalPosition(editArea.getPosition());
    areaNode.setLocalRotation(editArea.getRotation());
    areaNode.setParent(anchorNode);
  }
}
