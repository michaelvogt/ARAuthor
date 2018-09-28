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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.utils.ItemClickListener;
import eu.michaelvogt.ar.author.utils.LocationListAdapter;

public class LocationlistFragment extends Fragment implements ItemClickListener {
  private View view;

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

    RecyclerView recyclerView = view.findViewById(R.id.location_list);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(layoutManager);

    LocationListAdapter adapter = new LocationListAdapter(getContext());
    adapter.setItemClickListener(this);

    recyclerView.setAdapter(adapter);

    AuthorViewModel viewModel = ViewModelProviders.of(this).get(AuthorViewModel.class);
    viewModel.getAllLocations().observe(this, adapter::setLocations);
  }

  @Override
  public void onItemClicked(int position) {
    Bundle bundle = new Bundle();
    bundle.putInt("location_id", position);
    Navigation.findNavController(view).navigate(R.id.action_location_intro, bundle);
  }
}
