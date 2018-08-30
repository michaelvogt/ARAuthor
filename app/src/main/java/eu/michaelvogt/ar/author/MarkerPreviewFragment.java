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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.nodes.EventAnchorNode;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;

public class MarkerPreviewFragment extends Fragment {
  private static final String TAG = MarkerPreviewFragment.class.getSimpleName();

  private LoopArFragment arFragment;
  private AuthorViewModel viewModel;

  private Map<String, Node> handledImages = new HashMap<>();

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_markerpreview, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);

    arFragment = (LoopArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
    arFragment.getPlaneDiscoveryController().hide();
    arFragment.getPlaneDiscoveryController().setInstructionView(null);

    view.findViewById(R.id.listmarker_fab).setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.listFragment)
    );

    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
  }

  private void onUpdateFrame(FrameTime frameTime) {
    arFragment.onUpdate(frameTime);

    Frame frame = arFragment.getArSceneView().getArFrame();
    Collection<AugmentedImage> updatedAugmentedImages =
        frame.getUpdatedTrackables(AugmentedImage.class);

//    if (modelPoseOnPlaneListeners.size() != 0) {
//      for (ModelPoseOnPlaneListener listener : modelPoseOnPlaneListeners) {
//        if (listener.onPlane()) {
//          modelPoseOnPlaneListeners.remove(listener);
//        }
//      }
//    }

    for (AugmentedImage image : updatedAugmentedImages) {
      TrackingState trackingState = arFragment.getArSceneView().getArFrame().getCamera().getTrackingState();
      if (trackingState == TrackingState.TRACKING && !handledImages.containsKey(image.getName())) {
        handledImages.put(image.getName(), null);

        Anchor anchor = image.createAnchor(image.getCenterPose());
        EventAnchorNode anchorNode = new EventAnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        Marker marker = viewModel.getMarkerFromUid(Integer.parseInt(image.getName())).get();

        if (marker.isShowBackground()) {
          buildArea(anchorNode, Area.getBackgroundArea(marker, marker.getBackgroundImagePath()),
              (node) -> {
                buildAreas(node, marker.getAreaIds(), image);
              });
        } else {
          buildAreas(anchorNode, marker.getAreaIds(), image);
        }
      } else if (image.getTrackingState() == TrackingState.STOPPED) {
        handledImages = new HashMap<>();
      }
    }
  }

  private void buildAreas(Node anchorNode, List<Integer> areaIds, AugmentedImage image) {
    if (areaIds.size() > 0) {
      for (int areaId : areaIds) {
        buildArea(anchorNode, viewModel.getArea(areaId), null);
      }
    } else {
      // Build a default area for demo purposes
      buildArea(anchorNode, Area.getDefaultArea(image), null);
    }
  }

  private void buildArea(Node anchorNode, Area area, Consumer<Node> fn) {
    AreaNodeBuilder.builder(getContext(), viewModel, area)
        .build()
        .thenAccept(node -> {
          node.setParent(anchorNode);

          if (fn != null) {
            fn.accept(node);
          }

          Log.i(TAG, "Area successfully created " + area.getTitle());
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to build Area: " + area.getTitle(), throwable);
          return null;
        });
  }
}
