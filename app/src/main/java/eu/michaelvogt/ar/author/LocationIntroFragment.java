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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Location;
import eu.michaelvogt.ar.author.data.MarkerKt;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class LocationIntroFragment extends Fragment implements View.OnClickListener {
  private static final String TAG = LocationIntroFragment.class.getSimpleName();

  public LocationIntroFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_locationintro, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AuthorViewModel viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);

    WebView contentView = view.findViewById(R.id.content_info);
    contentView.setWebViewClient(new WebViewClient());
    contentView.getSettings().setBuiltInZoomControls(true);
    contentView.getSettings().setDisplayZoomControls(false);
    contentView.getSettings().setJavaScriptEnabled(true);

    long locationId = viewModel.getCurrentLocationId();

    viewModel.getLocation(locationId)
        .thenAccept(location -> getActivity().runOnUiThread(() -> initWebView(location, contentView)))
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to fetch location " + locationId, throwable);
          return null;
        });

    // Due to the way the AR images database needs to be initialized, and the markers are
    // delivered asynchronously from the data database, the markers need to be cached beforehand
    viewModel.getMarkersForLocation(locationId, MarkerKt.getMARKERS_AND_TITLES())
        .thenAccept(locations -> getActivity().runOnUiThread(() -> viewModel.setMarkersCache(locations)))
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to fetch markers for location " + locationId, throwable);
          return null;
        });

    View fab = view.findViewById(R.id.fab_map);
    fab.setOnClickListener(this);
  }

  private void initWebView(Location location, WebView contentView) {
    String path = null;
    try {
      path = FileUtils.getFullPuplicFolderLocalUrl(location.getIntroHtmlPath());
    } catch (Exception e) {
      Log.e(TAG, "Not able to load location intro of " + location.getName(), e);
    }

    contentView.loadUrl(path);
  }

  @Override
  public void onClick(View view) {
    Navigation.findNavController(view).navigate(R.id.action_preview_markers);
  }
}
