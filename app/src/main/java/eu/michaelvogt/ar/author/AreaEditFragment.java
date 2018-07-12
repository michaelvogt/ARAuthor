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
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AreaEditFragment extends Fragment {
  private AuthorViewModel viewModel;
  private Area editArea;

  private Switch useTranslucentSwitch;

  private EditText locationX;
  private EditText locationY;
  private EditText locationZ;
  private EditText rotationX;
  private EditText rotationY;
  private EditText rotationZ;
  private EditText rotationW;
  private EditText scaleX;
  private EditText scaleY;
  private EditText scaleZ;

  public AreaEditFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_area_edit, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
    int markerId = getArguments().getInt("marker_id");
    Marker editMarker = viewModel.getMarker(markerId);

    int areaIndex = getArguments().getInt("area_id");
    editArea = viewModel.getArea(editMarker.getAreaId(areaIndex));

    useTranslucentSwitch = view.findViewById(R.id.area_edit_display_translucent);
    useTranslucentSwitch.setChecked(getArguments().getInt("area_edit_translucency") == 1);

    locationX = view.findViewById(R.id.areaedit_xl_edit);
    locationX.setText(String.valueOf(editArea.getPosition().x));

    locationY = view.findViewById(R.id.areaedit_yl_edit);
    locationY.setText(String.valueOf(editArea.getPosition().y));

    locationZ = view.findViewById(R.id.areaedit_zl_edit);
    locationZ.setText(String.valueOf(editArea.getPosition().z));

    rotationX = view.findViewById(R.id.areaedit_xr_edit);
    rotationX.setText(String.valueOf(editArea.getRotation().x));

    rotationY = view.findViewById(R.id.areaedit_yr_edit);
    rotationY.setText(String.valueOf(editArea.getRotation().y));

    rotationZ = view.findViewById(R.id.areaedit_zr_edit);
    rotationZ.setText(String.valueOf(editArea.getRotation().z));

    rotationW = view.findViewById(R.id.areaedit_wr_edit);
    rotationW.setText(String.valueOf(editArea.getRotation().w));

    scaleX = view.findViewById(R.id.areaedit_xs_edit);
    scaleX.setText(String.valueOf(editArea.getScale().x));

    scaleY = view.findViewById(R.id.areaedit_ys_edit);
    scaleY.setText(String.valueOf(editArea.getScale().y));

    scaleZ = view.findViewById(R.id.areaedit_zs_edit);
    scaleZ.setText(String.valueOf(editArea.getScale().z));

    view.findViewById(R.id.areaedit_save).setOnClickListener(event -> {
      Vector3 location = new Vector3(asFloat(locationX), asFloat(locationY), asFloat(locationZ));
      Quaternion rotation = new Quaternion(
          asFloat(rotationX), asFloat(rotationY), asFloat(rotationZ), asFloat(rotationW));
      Vector3 scale = new Vector3(asFloat(scaleX), asFloat(scaleY), asFloat(scaleZ));

      editArea.setPosition(location);
      editArea.setRotation(rotation);
      editArea.setScale(scale);

      Bundle bundle = new Bundle();
      bundle.putInt("marker_id", getArguments().getInt("marker_id"));
      bundle.putInt("area_id", getArguments().getInt("area_id"));
      bundle.putInt("area_edit_translucency", useTranslucentSwitch.isChecked() ? 1 : 0);
      Navigation.findNavController(view).navigate(R.id.action_edit_area_placement, bundle);
    });
  }

  private float asFloat(EditText text) {
    return Float.parseFloat(text.getText().toString());
  }
}