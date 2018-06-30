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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Scene;

import java.util.Collection;

import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AreaFragment extends Fragment {
  private static final String TAG = AreaFragment.class.getSimpleName();

  private int areaIndex;
  private Marker editMarker;
  private Area editArea;
  private AuthorViewModel viewModel;

  private LoopArFragment arFragment;
  private AugmentedImage augmentedImage;

  private final Scene.OnPeekTouchListener peekTouchListener = new Scene.OnPeekTouchListener() {
    @Override
    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
      arFragment.onPeekTouch(hitTestResult, motionEvent);
    }
  };

  public AreaFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_area, container, false);
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
    arFragment.getArSceneView().getScene().setOnPeekTouchListener(peekTouchListener);
    arFragment.getPlaneDiscoveryController().hide();
    arFragment.getPlaneDiscoveryController().setInstructionView(null);

    // TODO: Currently all markers are imported. Restrict to the currently edited marker

    // create translucent outline renderable depending of area type
    // create node for renderable
    // attach to anchornode

    arFragment.getArSceneView().getScene().setOnUpdateListener(this::onUpdateFrame);
  }

  private void onUpdateFrame(FrameTime frameTime) {
    arFragment.onUpdate(frameTime);

    Frame frame = arFragment.getArSceneView().getArFrame();

    if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
      return;
    }

    Collection<AugmentedImage> updatedArImages = frame.getUpdatedTrackables(AugmentedImage.class);

    for (AugmentedImage image : updatedArImages)
      // Only the marker currently edited needs to be recognized
      if (image.getName().equals(editMarker.getTitle())) {
        switch (image.getTrackingState()) {
          case TRACKING:
            if (augmentedImage == null) {
              Anchor anchor = image.createAnchor(image.getCenterPose());
              AnchorNode anchorNode = new AnchorNode(anchor);
              anchorNode.setParent(arFragment.getArSceneView().getScene());

              augmentedImage = image;
              arFragment.getArSceneView().getScene().addChild(anchorNode);
            }
            break;

          case STOPPED:
            augmentedImage = null;
            break;
        }
      }
  }
}
