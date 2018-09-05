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
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.nodes.AuthorAnchorNode;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;
import eu.michaelvogt.ar.author.utils.SceneViewCallback;

public class PreviewFragment extends Fragment {
  private static final String TAG = PreviewFragment.class.getSimpleName();

  protected ViewGroup container;
  protected LoopArFragment arFragment;
  protected AuthorViewModel viewModel;

  protected Map<String, Node> handledImages = new HashMap<>();

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    container = (ViewGroup) view;
    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);

    arFragment = (LoopArFragment) getChildFragmentManager().findFragmentById(R.id.ar_fragment);

    arFragment.planeFindingMode = getArguments().getString("plane_finding_mode");
    arFragment.updateMode = getArguments().getString("update_mode");
    arFragment.focusMode = getArguments().getString("focus_mode");
    arFragment.lightEstimation = getArguments().getString("light_estimation");
  }

  protected void buildMarkerScene(Anchor anchor, Marker marker, float backgroundWidth, float backgroundHeight) {
    AuthorAnchorNode anchorNode = new AuthorAnchorNode(anchor, getContext(), container, new EventCallback());
    anchorNode.setParent(arFragment.getArSceneView().getScene());

    if (marker.isShowBackground()) {
      buildArea(anchorNode, Area.getBackgroundArea(marker, marker.getBackgroundImagePath()),
          (node) -> {
            buildAreas(node, marker.getAreaIds(), backgroundHeight, backgroundWidth);
            node.setLookDirection(Vector3.up(), anchorNode.getUp());
          });
    } else {
      buildAreas(anchorNode, marker.getAreaIds(), backgroundHeight, backgroundWidth);
    }
  }

  private void buildAreas(Node anchorNode, List<Integer> areaIds, float backgroundHeight, float backgroundWidth) {
    if (areaIds.size() > 0) {
      for (int areaId : areaIds) {
        buildArea(anchorNode, viewModel.getArea(areaId), null);
      }
    } else {
      // Build a default area for demo purposes
      buildArea(anchorNode, Area.getDefaultArea(backgroundHeight, backgroundWidth), null);
    }
  }

  private void buildArea(Node anchorNode, Area area, Consumer<Node> fn) {
    AreaNodeBuilder.builder(getActivity(), area)
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
