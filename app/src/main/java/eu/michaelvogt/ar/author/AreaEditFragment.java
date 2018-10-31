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
import android.widget.EditText;
import android.widget.Switch;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.AuthorViewModel;

public class AreaEditFragment extends Fragment {
  private static final String TAG = AreaEditFragment.class.getSimpleName();

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

  private AuthorViewModel viewModel;

  public AreaEditFragment() {
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_area_edit, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);

    long areaId = viewModel.getCurrentAreaId();
    viewModel.getAreaVisual(areaId)
        .thenAccept(areaVisual -> this.getActivity().runOnUiThread(() -> finishSetup(view, areaVisual)))
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to create area and set data.", throwable);
          return null;
        });
  }

  private void finishSetup(View view, AreaVisual editArea) {
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

    view.findViewById(R.id.areaedit_save).setOnClickListener(v -> handleSave(v, editArea));
    view.findViewById(R.id.areaedit_cancel).setOnClickListener(this::handleCancel);
    view.findViewById(R.id.areaedit_test).setOnClickListener(this::handleTest);
  }

  private void handleCancel(View view) {
    Bundle bundle = new Bundle();
    bundle.putInt("area_edit_translucency", useTranslucentSwitch.isChecked() ? 1 : 0);
    Navigation.findNavController(view).navigate(R.id.action_edit_marker, bundle);
  }

  private void handleTest(View view) {
    Bundle bundle = new Bundle();
    bundle.putInt("area_edit_translucency", useTranslucentSwitch.isChecked() ? 1 : 0);
    Navigation.findNavController(view).navigate(R.id.action_test_area_placement, bundle);
  }

  private void handleSave(View view, AreaVisual editArea) {
    Vector3 location = new Vector3(asFloat(locationX), asFloat(locationY), asFloat(locationZ));
    Quaternion rotation = new Quaternion(
        asFloat(rotationX), asFloat(rotationY), asFloat(rotationZ), asFloat(rotationW));
    Vector3 scale = new Vector3(asFloat(scaleX), asFloat(scaleY), asFloat(scaleZ));

    Area area = editArea.getArea();
    area.setPosition(location);
    area.setRotation(rotation);
    area.setScale(scale);
    viewModel.updateArea(area);

    Bundle bundle = new Bundle();
    bundle.putInt("area_edit_translucency", useTranslucentSwitch.isChecked() ? 1 : 0);
    Navigation.findNavController(view).navigate(R.id.action_edit_marker, bundle);
  }

  private float asFloat(EditText text) {
    return Float.parseFloat(text.getText().toString());
  }
}
