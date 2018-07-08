package eu.michaelvogt.ar.author;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;


/**
 * A simple {@link Fragment} subclass.
 */
public class AreaEditFragment extends Fragment {
  private AuthorViewModel viewModel;

  private EditText locationX;
  private EditText locationY;
  private EditText locationZ;
  private EditText rotationX;
  private EditText rotationY;
  private EditText rotationZ;
  private EditText rotationW;

  public AreaEditFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_area_edit, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    locationX = view.findViewById(R.id.areaedit_xl_edit);
    locationX.setText(String.valueOf(getArguments().getFloat("loc_x")));

    locationY = view.findViewById(R.id.areaedit_yl_edit);
    locationY.setText(String.valueOf(getArguments().getFloat("loc_y")));

    locationZ = view.findViewById(R.id.areaedit_zl_edit);
    locationZ.setText(String.valueOf(getArguments().getFloat("loc_z")));

    rotationX = view.findViewById(R.id.areaedit_xr_edit);
    rotationX.setText(String.valueOf(getArguments().getFloat("rot_x")));

    rotationY = view.findViewById(R.id.areaedit_yr_edit);
    rotationY.setText(String.valueOf(getArguments().getFloat("rot_y")));

    rotationZ = view.findViewById(R.id.areaedit_zr_edit);
    rotationZ.setText(String.valueOf(getArguments().getFloat("rot_z")));

    rotationW = view.findViewById(R.id.areaedit_wr_edit);
    rotationW.setText(String.valueOf(getArguments().getFloat("rot_w")));

    view.findViewById(R.id.areaedit_save).setOnClickListener(event -> {
      viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
      int markerId = getArguments().getInt("markerid");
      Marker editMarker = viewModel.getMarker(markerId);

      int areaIndex = getArguments().getInt("areaid");
      String areaId = editMarker.getAreaId(areaIndex);
      Area editArea = viewModel.getArea(areaId).get();

      Vector3 location = new Vector3(asFloat(locationX), asFloat(locationY), asFloat(locationZ));
      Quaternion rotation = new Quaternion(
          asFloat(rotationX), asFloat(rotationY), asFloat(rotationZ), asFloat(rotationW));

      editArea.setPosition(location);
      editArea.setRotation(rotation);

      Bundle bundle = new Bundle();
      bundle.putInt("markerid", getArguments().getInt("markerid"));
      bundle.putInt("areaid", getArguments().getInt("areaid"));
      Navigation.findNavController(view).navigate(R.id.action_edit_area_placement, bundle);
    });
  }

  private float asFloat(EditText text) {
    return Float.parseFloat(text.getText().toString());
  }
}
