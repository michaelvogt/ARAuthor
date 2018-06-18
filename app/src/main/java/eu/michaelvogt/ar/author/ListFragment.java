package eu.michaelvogt.ar.author;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
    private View mView;

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

        mView = view;

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
        Navigation.findNavController(mView).navigate(R.id.action_edit_marker, bundle);
    }
}
