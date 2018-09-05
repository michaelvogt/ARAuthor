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
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Location;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class LocationIntroFragment extends Fragment implements View.OnClickListener {
  private static final String TAG = LocationIntroFragment.class.getSimpleName();

  private View view;
  private int locationId;

  public LocationIntroFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_locationintro, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.view = view;

    AuthorViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AuthorViewModel.class);
    locationId = getArguments().getInt("location_id");
    Location location = viewModel.getLocation(locationId);

    WebView contentView = view.findViewById(R.id.content_info);
    contentView.setWebViewClient(new WebViewClient());
    contentView.getSettings().setBuiltInZoomControls(true);
    contentView.getSettings().setDisplayZoomControls(false);
    contentView.getSettings().setJavaScriptEnabled(true);

    View fab = view.findViewById(R.id.fab_map);

    initWebView(location, contentView);

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
    Bundle bundle = new Bundle();
    bundle.putInt("location_id", locationId);
    Navigation.findNavController(view).navigate(R.id.action_preview_markers, bundle);
  }
}
