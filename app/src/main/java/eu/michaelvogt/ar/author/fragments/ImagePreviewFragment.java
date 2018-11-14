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

package eu.michaelvogt.ar.author.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.R;

public class ImagePreviewFragment extends PreviewFragment {
  private static final String TAG = ImagePreviewFragment.class.getSimpleName();

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_imagepreview, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    arFragment.getPlaneDiscoveryController().hide();
    arFragment.getPlaneDiscoveryController().setInstructionView(null);

    arFragment.hasMarker = false;

    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

    view.findViewById(R.id.listmarker_fab).setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    arFragment.changeGrabbedOrientation(newConfig.orientation);
  }

  private void onUpdateFrame(FrameTime frameTime) {
    arFragment.onUpdate(frameTime);

    Frame frame = arFragment.getArSceneView().getArFrame();
    Collection<AugmentedImage> updatedAugmentedImages =
        frame.getUpdatedTrackables(AugmentedImage.class);

    for (AugmentedImage image : updatedAugmentedImages) {
      TrackingState trackingState = arFragment.getArSceneView().getArFrame().getCamera().getTrackingState();
      if (trackingState == TrackingState.TRACKING && !handledImages.containsKey(image.getName())) {
        handledImages.put(image.getName(), null);
        Anchor anchor = image.createAnchor(image.getCenterPose());
        viewModel.getMarker(Integer.parseInt(image.getName()))
            .thenAccept(marker ->
                buildMarkerScene(anchor, marker, image.getExtentX(), image.getExtentZ()))
            .exceptionally(throwable -> {
              Log.e(TAG, "Unable to fetch marker " + image.getName(), throwable);
              return null;
            });
      } else if (image.getTrackingState() == TrackingState.STOPPED) {
        handledImages.remove(image.getName());
      }
    }
  }
}

