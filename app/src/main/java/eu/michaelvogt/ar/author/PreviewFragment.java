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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import eu.michaelvogt.ar.author.data.AreaKt;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.nodes.AreaNode;
import eu.michaelvogt.ar.author.nodes.AuthorAnchorNode;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;
import eu.michaelvogt.ar.author.utils.SceneViewCallback;

public class PreviewFragment extends Fragment {
  private static final String TAG = PreviewFragment.class.getSimpleName();

  protected ViewGroup container;
  protected LoopArFragment arFragment;
  protected AuthorViewModel viewModel;

  protected Map<String, Node> handledImages = new HashMap<>();
  protected List<Node> cameraFacingNodes = new ArrayList<>();

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    container = (ViewGroup) view;
    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);

    arFragment = (LoopArFragment) getChildFragmentManager().findFragmentById(R.id.ar_fragment);
    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

    arFragment.planeFindingMode = getArguments().getString("plane_finding_mode");
    arFragment.updateMode = getArguments().getString("update_mode");
    arFragment.focusMode = getArguments().getString("focus_mode");
    arFragment.lightEstimation = getArguments().getString("light_estimation");
  }

  private void onUpdateFrame(FrameTime frameTime) {
    arFragment.onUpdate(frameTime);

    cameraFacingNodes.forEach(node -> {
      Vector3 cameraPosition = arFragment.getArSceneView().getScene().getCamera().getWorldPosition();
      Vector3 cardPosition = node.getWorldPosition();
      Vector3 direction = Vector3.subtract(cameraPosition, cardPosition);
      Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
      node.setWorldRotation(lookRotation);
    });

//    if (modelPoseOnPlaneListeners.size() != 0) {
//      for (ModelPoseOnPlaneListener listener : modelPoseOnPlaneListeners) {
//        if (listener.onPlane()) {
//          modelPoseOnPlaneListeners.remove(listener);
//        }
//      }
//    }
  }

  protected void buildMarkerScene(Anchor anchor, Marker marker, float backgroundWidth, float backgroundHeight) {
    AuthorAnchorNode anchorNode = new AuthorAnchorNode(anchor, getContext(), container, new EventCallback());
    anchorNode.setParent(arFragment.getArSceneView().getScene());

    if (marker.isShowBackground()) {
      buildArea(anchorNode, AreaVisual.Companion.getBackgroundArea(marker, marker.getBackgroundImagePath()),
          (node) -> {
            buildAreas(node, marker.getUId(), backgroundHeight, backgroundWidth);
            node.setLookDirection(Vector3.up(), anchorNode.getUp());
          });
    } else {
      buildAreas(anchorNode, marker.getUId(), backgroundHeight, backgroundWidth);
    }
  }

  private void buildAreas(Node anchorNode, long markerId, float backgroundHeight, float backgroundWidth) {
    viewModel.getAreaVisualsForMarker(markerId, AreaKt.GROUP_START).thenAccept(areaVisuals -> {
      if (areaVisuals.size() > 0) {
        areaVisuals.forEach(areaVisual -> buildArea(anchorNode, areaVisual, null));
      } else {
        // Build a default area for demo purposes
        buildArea(anchorNode, AreaVisual.Companion.getDefaultArea(backgroundHeight, backgroundWidth), null);
      }
    }).exceptionally(throwable -> {
      Log.e(TAG, "Unable to build Areas: " + markerId, throwable);
      return null;
    });
  }

  private void buildArea(Node anchorNode, AreaVisual areaVisual, Consumer<Node> fn) {
    this.getActivity().runOnUiThread(() -> AreaNodeBuilder
        .builder(getActivity(), areaVisual)
        .build()
        .thenAccept(node -> {
          node.setParent(anchorNode);

          if (fn != null) {
            fn.accept(node);
          }

          if (((AreaNode) node).isCameraFacing()) {
            cameraFacingNodes.add(node);
          }

          Log.i(TAG, "Area successfully created " + areaVisual.getTitle());
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build Area: " + areaVisual.getTitle(), throwable);
          return null;
        }));
  }

  public class EventCallback implements SceneViewCallback {
    @Override
    public void pause() {
      arFragment.getArSceneView().pause();
    }

    @Override
    public void resume() throws CameraNotAvailableException {
      arFragment.getArSceneView().resume();
    }
  }
}
