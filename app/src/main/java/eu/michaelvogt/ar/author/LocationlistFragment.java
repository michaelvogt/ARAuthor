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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.utils.ItemClickListener;
import eu.michaelvogt.ar.author.utils.LocationListAdapter;

public class LocationlistFragment extends Fragment implements ItemClickListener {
  private static final String TAG = LocationlistFragment.class.getSimpleName();

  private View view;
  private AuthorViewModel viewModel;

  public LocationlistFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_locationlist, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.view = view;

    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);

    RecyclerView recyclerView = view.findViewById(R.id.location_list);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(layoutManager);

    LocationListAdapter adapter = new LocationListAdapter(getContext());
    adapter.setItemClickListener(this);

    recyclerView.setAdapter(adapter);

    Log.i(TAG, "Fetch all locations");

    viewModel.getAllLocations()
        .thenAccept(locations -> getActivity().runOnUiThread(() -> adapter.setLocations(locations)))
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to fetch all locations.", throwable);
          return null;
        });
  }

  @Override
  public void onItemClicked(long locationId) {

    Log.i(TAG, "Set currentLocationId " + locationId);

    viewModel.setCurrentLocationId(locationId);
    Navigation.findNavController(view).navigate(R.id.action_location_intro);
  }
}
