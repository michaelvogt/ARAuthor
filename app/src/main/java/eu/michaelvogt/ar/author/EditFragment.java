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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.utils.ImageUtils;


public class EditFragment extends Fragment {
  private static final String TAG = EditFragment.class.getSimpleName();
  private static final int NUM_TABS = 3;

  private int editIndex;
  private Marker editMarker;
  private ImageView markerImage;
  private AuthorViewModel viewModel;

  private EditFragmentMarker tabMarker;
  private Fragment tabInfo;
  private Fragment tabAreas;

  public EditFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_editmarker, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
    editIndex = getArguments().getInt("edit_index");
    Marker cropMarker = viewModel.getCropMarker();

    if (editIndex == -1) {
      editMarker = new Marker();
      if (cropMarker != null) {
        editMarker = cropMarker;
        viewModel.clearCropMarker();
      }
    } else {
      editMarker = viewModel.getEditMarker(editIndex);
    }

    markerImage = view.findViewById(R.id.image_marker);
    if (editMarker.hasImage()) {
      markerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
          editMarker.getMarkerImagePath(), 300, 300));
    }

    TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(), editMarker);
    ViewPager tabPager = view.findViewById(R.id.editmarker_pager);
    tabPager.setAdapter(tabAdapter);

    TabLayout tabLayout = view.findViewById(R.id.editmarker_tabs);
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override public void onTabSelected(TabLayout.Tab tab) {

      }

      @Override public void onTabUnselected(TabLayout.Tab tab) { }
      @Override public void onTabReselected(TabLayout.Tab tab) { }
    });

    view.findViewById(R.id.button_save).setOnClickListener(this::handleSave);
  }

  private void handleSave(View view) {
    if (editIndex == -1) {
      viewModel.addMarker(editMarker);
    } else {
      viewModel.setMarker(editIndex, editMarker);
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
          tabMarker = EditFragmentMarker.instantiate(editMarker, viewModel, new UpdateMarkerHandler() {
            @Override
            public void updateMarker(Bitmap bitmap) {
              markerImage.setImageBitmap(bitmap);
            }

            @Override
            public void updateMarker(int resId) {
              markerImage.setImageResource(resId);
            }
          });
          return tabMarker;
        case 1:
          tabInfo = EditFragmentInfo.instantiate(editMarker);
          return tabInfo;
        case 2:
          tabAreas = EditFragmentAreas.instantiate(editIndex);
          return tabAreas;
        default:
          throw new IllegalArgumentException("Wrong edit marker tab requested: " + position);
      }
    }
  }

  public interface UpdateMarkerHandler {
    void updateMarker(Bitmap bitmap);
    void updateMarker(int ic_launcher);
  }
}
