/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.nodes.AuthorAnchorNode;
import eu.michaelvogt.ar.author.utils.FileUtils;
import eu.michaelvogt.ar.author.utils.ImageUtils;

public class LoopArFragment extends ArFragment {
  private static final String TAG = LoopArFragment.class.getSimpleName();

  boolean hasMarker;
  String planeFindingMode;
  String updateMode;
  String focusMode;
  String lightEstimation;

  @Override
  protected Config getSessionConfiguration(Session session) {
    AuthorViewModel viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
    AugmentedImageDatabase imagedb = new AugmentedImageDatabase(session);

    if (hasMarker) {
      List<Marker> markers = viewModel.getMarkersCache();
      if (markers != null) {
        for (Marker marker : markers) {
          try {
            String path = FileUtils.getFullPuplicFolderPath(marker.getMarkerImagePath());
            Bitmap bitmap = ImageUtils.INSTANCE.decodeSampledBitmapFromImagePath(path, Marker.MIN_SIZE, Marker.MIN_SIZE);
            if (bitmap != null) {
              int index = marker.getWidthInM() <= 0
                  ? imagedb.addImage(String.valueOf(marker.getUId()), bitmap)
                  : imagedb.addImage(String.valueOf(marker.getUId()), bitmap, marker.getWidthInM());
              Log.d(TAG, "marker - " + marker.getTitle() + "(" + index + ")" + " imported");
            } else {
              Log.d(TAG, "marker - " + marker.getTitle() + " NOT imported");
              Snackbar.make(getView(), "marker " + marker.getTitle() + " NOT imported",
                  Snackbar.LENGTH_SHORT).show();
            }
          } catch (Exception ex) {
            Log.e(TAG, "Something bad happened", ex);
            Snackbar.make(getView(), "Exception: " + ex.getMessage(),
                Snackbar.LENGTH_SHORT).show();
          }
        }
      }
    }

    Config config = new Config(session);
    config.setAugmentedImageDatabase(imagedb);

    config.setUpdateMode(Config.UpdateMode.valueOf(updateMode));
    config.setPlaneFindingMode(Config.PlaneFindingMode.valueOf(planeFindingMode));
    config.setFocusMode(Config.FocusMode.valueOf(focusMode));
    config.setLightEstimationMode(Config.LightEstimationMode.valueOf(lightEstimation));

    return config;
  }

  public void changeGrabbedOrientation(int configOrientation) {
    getArSceneView().getScene().callOnHierarchy(node -> {
      if (node instanceof AuthorAnchorNode) {
        ((AuthorAnchorNode) node).changeGrabbedOrientation(configOrientation);
      }
    });
  }
}