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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.AuthorViewModelKt;
import eu.michaelvogt.ar.author.utils.ItemClickListener;
import eu.michaelvogt.ar.author.utils.MarkerListAdapter;

public class MarkerListFragment extends Fragment implements ItemClickListener {
  private static final String TAG = MarkerListFragment.class.getSimpleName();

  private View view;
  private AuthorViewModel viewModel;

  public MarkerListFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_markerlist, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.view = view;

    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
    long locationId = viewModel.getCurrentLocationId();

    RecyclerView recyclerView = view.findViewById(R.id.marker_list);
    recyclerView.setHasFixedSize(false);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(layoutManager);

    MarkerListAdapter adapter = new MarkerListAdapter(getContext());
    recyclerView.setAdapter(adapter);

    viewModel.getMarkersForLocation(locationId, true)
        .thenAccept(adapter::setMarkers)
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to fetch markers for location " + locationId, throwable);
          return null;
        });

    adapter.setItemClickListener(this);

    view.findViewById(R.id.editmarker_fab).setOnClickListener(v -> {
      viewModel.setCurrentMarkerId(AuthorViewModelKt.NEW_CURRENT_MARKER);
      Navigation.findNavController(view).navigate(R.id.action_edit_marker);
    });

//        view.findViewById(R.id.button_importmarkers).setOnClickListener();

    view.findViewById(R.id.button_testmarkers).setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.action_test_markers)
    );
  }

  @Override
  public void onItemClicked(long uId) {
    viewModel.setCurrentMarkerId(uId);
    Navigation.findNavController(view).navigate(R.id.action_edit_marker);
  }
}
