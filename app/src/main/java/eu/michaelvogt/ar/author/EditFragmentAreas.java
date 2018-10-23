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
import eu.michaelvogt.ar.author.data.AreaKt;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.utils.AreaListAdapter;
import eu.michaelvogt.ar.author.utils.ItemClickListener;

public class EditFragmentAreas extends Fragment implements ItemClickListener {
  private View view;

  private long markerId;

  public EditFragmentAreas() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_editmarker_areas, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.view = view;
    AuthorViewModel viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);

    RecyclerView recyclerView = view.findViewById(R.id.areas_list);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(mLayoutManager);

    AreaListAdapter adapter = new AreaListAdapter(getContext());
    adapter.setItemClickListener(this);

    recyclerView.setAdapter(adapter);

    viewModel.getAreasForMarker(markerId, AreaKt.GROUP_ALL).thenAccept(adapter::setAreas);
  }

  @Override
  public void onItemClicked(long position) {
    Bundle bundle = new Bundle();
    bundle.putLong("marker_id", markerId);
    bundle.putLong("area_id", position);
    Navigation.findNavController(view).navigate(R.id.action_edit_area_placement, bundle);
  }

  private void setMarkerId(long markerId) {
    this.markerId = markerId;
  }

  public static Fragment instantiate(long markerId) {
    EditFragmentAreas tabFragment = new EditFragmentAreas();
    tabFragment.setMarkerId(markerId);
    return tabFragment;
  }
}
