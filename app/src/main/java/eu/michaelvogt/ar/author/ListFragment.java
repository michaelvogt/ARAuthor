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

import java.util.Objects;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.utils.MarkerListAdapter;


public class ListFragment extends Fragment implements MarkerListAdapter.OnItemClickListener {
    private View view;

    public ListFragment() {/* Required empty public constructor*/}

  @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        RecyclerView mRecyclerView = view.findViewById(R.id.marker_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        MarkerListAdapter mAdapter = new MarkerListAdapter(
                ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AuthorViewModel.class));
        mAdapter.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.editmarker_fab).setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.editFragment)
        );

//        view.findViewById(R.id.button_importmarkers).setOnClickListener();

        view.findViewById(R.id.button_testmarkers).setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_test_markers)
        );
    }

  @Override
    public void onItemClicked(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("edit_index", position);
        Navigation.findNavController(view).navigate(R.id.action_edit_marker, bundle);
    }
}
