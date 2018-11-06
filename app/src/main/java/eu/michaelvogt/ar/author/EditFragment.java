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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;


public class EditFragment extends Fragment {
  private static final String TAG = EditFragment.class.getSimpleName();
  private static final int NUM_TABS = 3;

  private long editMarkerId;
  private Marker editMarker;
  private AuthorViewModel viewModel;
  private View view;

  public EditFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_editmarker, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.view = view;

    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
    editMarkerId = viewModel.getCurrentMarkerId();
    Marker cropMarker = viewModel.getCropMarker();

    if (editMarkerId == -1) {
      editMarker = new Marker();
      if (cropMarker != null) {
        editMarker = cropMarker;
        viewModel.clearCropMarker();
      }
      finishSetup();
    } else {
      viewModel.getMarker(editMarkerId)
          .thenAccept(marker -> {
            editMarker = marker;
            getActivity().runOnUiThread(this::finishSetup);
          })
          .exceptionally(throwable -> {
            Log.e(TAG, "Unable to fetch marker " + editMarkerId, throwable);
            return null;
          });
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.actionbar_editmarker_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.actionbar_editmarker_save:
        handleSave();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void finishSetup() {
    TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(), editMarker);
    ViewPager tabPager = view.findViewById(R.id.editmarker_pager);
    tabPager.setAdapter(tabAdapter);
  }

  private void handleAr(View view) {
    Bundle bundle = new Bundle();
    bundle.putString("plane_finding_mode", "VERTICAL");
    bundle.putInt("discovery_controller", 1);
    Navigation.findNavController(view).navigate(R.id.action_marker_preview, bundle);
  }

  private void handleSave() {
    if (editMarkerId == -1) {
      editMarker.setLocationId(viewModel.getCurrentLocationId());
      viewModel.addMarker(editMarker);
    } else {
      viewModel.updateMarker(editMarker);
    }

    Navigation.findNavController(view).navigate(R.id.action_list_markers);
  }

  private class TabAdapter extends FragmentPagerAdapter {
    private String[] tabTitles = new String[]{"Marker", "Info", "Areas"};

    TabAdapter(FragmentManager fragmentManager, Marker editIndex) {
      super(fragmentManager);
    }

    @Override
    public int getCount() {
      return NUM_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return EditFragmentMarker.instantiate(editMarker, viewModel);
        case 1:
          return EditFragmentInfo.instantiate(editMarker);
        case 2:
          return EditFragmentAreas.instantiate(editMarkerId);
        default:
          throw new IllegalArgumentException("Requested edit marker tab doesn't exist: " + position);
      }
    }
  }
}
