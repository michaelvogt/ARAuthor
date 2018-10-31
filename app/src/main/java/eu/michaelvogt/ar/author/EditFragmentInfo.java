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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import eu.michaelvogt.ar.author.data.Marker;

public class EditFragmentInfo extends Fragment implements PopupMenu.OnMenuItemClickListener {
  private Marker marker;

  private Button popupButton;
  private TextView introText;

  public EditFragmentInfo() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_editmarker_info, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    introText = view.findViewById(R.id.edit_intro);
    introText.setText(marker.getIntro());
    introText.setOnFocusChangeListener((editView, hasFocus) -> {
      if (!hasFocus) {
        marker.setIntro(introText.getText().toString());
      }
    });


    popupButton = view.findViewById(R.id.edit_type);
    popupButton.setOnClickListener(popupView -> {
        PopupMenu popup = new PopupMenu(getContext(), popupView);
        popup.inflate(R.menu.marker_object_types);
        popup.setOnMenuItemClickListener(EditFragmentInfo.this);
        popup.show();
    });
  }

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    popupButton.setText(item.getTitle());

    switch (item.getItemId()) {
      case R.id.marker_type_building:
        return true;
      case R.id.marker_type_location:
        return true;
      case R.id.marker_type_infoboard:
        return true;
      default:
        return false;
    }
  }

  private void setMarker(Marker marker) {
    this.marker = marker;
  }

  public static Fragment instantiate(Marker marker) {
    EditFragmentInfo tabFragment = new EditFragmentInfo();
    tabFragment.setMarker(marker);
    return tabFragment;
  }
}
