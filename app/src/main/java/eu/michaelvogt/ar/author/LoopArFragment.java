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
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.ux.ArFragment;

import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.utils.ImageUtils;

public class LoopArFragment extends ArFragment {
    private static  final String TAG = LoopArFragment.class.getSimpleName();

    @Override
    protected Config getSessionConfiguration(Session session) {
        AuthorViewModel viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
        AugmentedImageDatabase imagedb = new AugmentedImageDatabase(session);

        Bitmap bitmap;
        for (Marker marker : viewModel.markerIterable()) {
            try {
                bitmap = ImageUtils.decodeSampledBitmapFromImagePath(
                        marker.getImagePath(), Marker.MIN_SIZE, Marker.MIN_SIZE);
                if (bitmap != null) {
                    int index = marker.getWidthInM() <= 0
                            ? imagedb.addImage(marker.getTitle(), bitmap)
                            : imagedb.addImage(marker.getTitle(), bitmap, marker.getWidthInM());
                    Log.d(TAG, "marker " + marker.getTitle() + "("+ index +")" + " imported");
                } else {
                    Log.d(TAG, "marker " + marker.getTitle() + " NOT imported");
                    Snackbar.make(getView(), "marker " + marker.getTitle() + " NOT imported",
                            Snackbar.LENGTH_SHORT).show();
                }
            } catch (Throwable ex) {
                Log.e(TAG, "Something bad happened", ex);
                Snackbar.make(getView(), "Exception: " + ex.getMessage(),
                        Snackbar.LENGTH_SHORT).show();
            }
        }

        Config config = new Config(session);
        config.setAugmentedImageDatabase(imagedb);

        return config;
    }
}