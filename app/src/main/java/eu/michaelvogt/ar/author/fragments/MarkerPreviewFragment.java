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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.AuthorViewModel;

public class MarkerPreviewFragment extends PreviewFragment {
  private static final String TAG = ImagePreviewFragment.class.getSimpleName();

  private boolean isSceneDropped;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_markerpreview, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    arFragment.getPlaneDiscoveryController().show();
    arFragment.hasMarker = true;
    arFragment.setOnTapArPlaneListener(this::onTapArPlane);

    view.findViewById(R.id.listmarker_fab).setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.marker_list_fragment)
    );
  }

  private void onTapArPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
    if (!isSceneDropped) {
      if ((plane.getTrackingState() == TrackingState.TRACKING)) {
        isSceneDropped = true;

        AuthorViewModel viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
        Anchor anchor = hitResult.createAnchor();

        this.viewModel.getMarker(viewModel.getCurrentMarkerId())
            .thenAccept(marker ->
                getActivity().runOnUiThread(() -> buildMarkerScene(anchor, marker, marker.getSize().x, marker.getSize().z))
            )
            .exceptionally(throwable -> {
              Log.e(TAG, "Unable to fetch marker " + viewModel.getCurrentMarkerId(), throwable);
              return null;
            });
      } else if (plane.getTrackingState() == TrackingState.STOPPED) {
        isSceneDropped = false;
      }
    }
  }
}

