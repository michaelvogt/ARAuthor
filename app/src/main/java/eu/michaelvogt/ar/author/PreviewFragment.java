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


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Collection;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.locations.goryoukaku.Office;
import eu.michaelvogt.ar.author.locations.iwamiginzan.Hidakaya;
import eu.michaelvogt.ar.author.locations.iwamiginzan.Kumagaike;

public class PreviewFragment extends Fragment {
  private LoopArFragment loopArFragment;

  private Node butterfly;
  private ModelRenderable butterflyRenderable;

  private boolean hidakainfoDone;
  private boolean kumagaikeinfoDone;
  private boolean butterflyDone;
  private boolean officeinfoDone;


  private final Scene.OnPeekTouchListener peekTouchListener = new Scene.OnPeekTouchListener() {
    @Override
    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
      loopArFragment.onPeekTouch(hitTestResult, motionEvent);
    }
  };

  public PreviewFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_preview, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    loopArFragment = (LoopArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
    loopArFragment.getArSceneView().getScene().setOnPeekTouchListener(peekTouchListener);
    loopArFragment.getPlaneDiscoveryController().hide();
    loopArFragment.getPlaneDiscoveryController().setInstructionView(null);

    ModelRenderable.builder()
        .setSource(getContext(), R.raw.monarch)
        .build()
        .thenAccept(renderable -> butterflyRenderable = renderable)
        .exceptionally(throwable -> {
              Log.e(Constraints.TAG, "Unable to load butterfly Renderable.", throwable);
              return null;
            });

    loopArFragment.setOnFrameListener((frameTime, frame) -> {
      Collection<AugmentedImage> updatedAugmentedImages =
          frame.getUpdatedTrackables(AugmentedImage.class);

      if (updatedAugmentedImages.size() == 1) {
        for (AugmentedImage image : updatedAugmentedImages) {
          if (image.getTrackingState() == TrackingState.TRACKING) {
            Anchor anchor = image.createAnchor(image.getCenterPose());
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(loopArFragment.getArSceneView().getScene());

            switch (image.getName()) {
              case Hidakaya.SIGN:
                if (!hidakainfoDone) {
                  hidakainfoDone = true;
                  Hidakaya.displayInfoScene(getContext(), anchorNode, image);
                }
                break;
              case Kumagaike.SIGN:
                if (!kumagaikeinfoDone) {
                  kumagaikeinfoDone = true;
                  Kumagaike.displayInfoScene(getContext(), anchorNode, image);
                }
                break;
              case Office.SIGN:
                if (!officeinfoDone) {
                  officeinfoDone = true;
                  new Office().displayInfoScene(getContext(),
                      anchorNode, image, loopArFragment.getArSceneView().getSession());
                }
                break;
              default:
                if (!butterflyDone) {
                  butterflyDone = true;
                  makeButterfly(anchor, image);
                }
                break;
            }
          }
        }
      }
    });

    view.findViewById(R.id.listmarker_fab).setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.listFragment)
    );
  }

  private void makeButterfly(Anchor anchor, AugmentedImage image) {
    if (this.butterfly != null)
      return;

    AnchorNode anchorNode = new AnchorNode(anchor);
    anchorNode.setParent(loopArFragment.getArSceneView().getScene());

    // Create the transformable butterfly and add it to the anchor.
    Node butterfly = new Node();
    butterfly.setRenderable(butterflyRenderable);
    butterfly.setLocalPosition(new Vector3(-image.getExtentX(), 0, -image.getExtentZ()));
    butterfly.setLocalRotation(new Quaternion(new Vector3(0, 1, 0 ), 180));

    butterfly.setParent(anchorNode);

    this.butterfly = butterfly;
  }
}
