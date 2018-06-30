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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.utils.FileUtils;
import eu.michaelvogt.ar.author.utils.ImageUtils;

import static android.app.Activity.RESULT_OK;

public class EditFragmentMarker extends Fragment {
  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final int REQUEST_PICK_IMAGE = 2;

  private TextView editLocation;
  private TextView editTitle;
  private TextView editWidth;

  private Marker editMarker;
  private AuthorViewModel viewModel;
  private EditFragment.UpdateMarkerHandler markerHandler;

  public EditFragmentMarker() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_editmarker_marker, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // TODO: Use value binding
    editTitle = view.findViewById(R.id.edit_title);
    editTitle.setText(editMarker.getTitle());
    editTitle.setOnFocusChangeListener((editView, hasFocus) -> {
      if (!hasFocus) {
        editMarker.setTitle(editTitle.getText().toString());
      }
    });

    editLocation = view.findViewById(R.id.edit_location);
    editLocation.setText(editMarker.getLocation());
    editLocation.setOnFocusChangeListener((editView, hasFocus) -> {
      if (!hasFocus) {
        editMarker.setLocation(editLocation.getText().toString());
      }
    });

    editWidth = view.findViewById(R.id.edit_width);
    editWidth.setText(String.valueOf(editMarker.getWidthInM()));
    editWidth.setOnFocusChangeListener((editView, hasFocus) -> {
      if (!hasFocus) {
        editMarker.setWidthInM(Float.parseFloat(editWidth.getText().toString()));
      }
    });

    view.findViewById(R.id.button_capture).setOnClickListener(this::handleCapture);
    view.findViewById(R.id.button_import).setOnClickListener(view13 -> handleImport());
    view.findViewById(R.id.button_edit).setOnClickListener(v -> handleCrop(view));
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      markerHandler.updateMarker(ImageUtils.decodeSampledBitmapFromImagePath(
          editMarker.getImagePath(), 100, 100));
    } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
      Uri selectedImage = data.getData();
      String[] filePathColumn = {MediaStore.Images.Media.DATA};

      Cursor cursor = getActivity().getContentResolver().query(selectedImage,
          filePathColumn, null, null, null);

      if (cursor != null) {
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagePath = cursor.getString(columnIndex);
        cursor.close();

        String photoPath;

        try {
          File sourceFile = new File(imagePath);
          photoPath = FileUtils.MARKERS_PATH + sourceFile.getName();
          FileUtils.copyFile(sourceFile, FileUtils.getFullPuplicFolderFile(photoPath));
        } catch (IOException e) {
          photoPath = "";
          markerHandler.updateMarker(R.drawable.ic_launcher);
          e.printStackTrace();
        }

        markerHandler.updateMarker(ImageUtils.decodeSampledBitmapFromImagePath(
            photoPath, 100, 100));

        editMarker.setImagePath(photoPath);
      }
    }
  }

  private void setMarker(Marker marker) {
    this.editMarker = marker;
  }

  private void setViewModel(AuthorViewModel viewModel) {
    this.viewModel = viewModel;
  }

  private void setMarkerHandler(EditFragment.UpdateMarkerHandler markerHandler) {
    this.markerHandler = markerHandler;
  }

  private void handleCrop(View view) {
    viewModel.setCropMarker(editMarker);
    Navigation.findNavController(view).navigate(R.id.action_crop_marker_image);
  }

  private void handleImport() {
    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
    photoPickerIntent.setType("image/*");
    startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
  }


  private void handleCapture(View view) {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
      // Create the File where the photo should go
      File photoFile = null;
      try {
        photoFile = createImageFile("marker_", FileUtils.MARKERS_PATH);
      } catch (IOException ex) {
        Log.d("EditFragment", ex.getLocalizedMessage());
      }

      // Continue only if the File was successfully created
      if (photoFile != null) {
        getActivity().getPackageManager();
        Uri photoURI = FileProvider.getUriForFile(getContext(),
            "eu.michaelvogt.ar.author.fileprovider",
            photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
      }
    }
  }

  private File createImageFile(String filePrefix, String path) throws IOException {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = filePrefix + timeStamp + "_";
    File storageDir = FileUtils.getFullPuplicFolderFile(path);
    File image = File.createTempFile(imageFileName, ".jpg", storageDir);

    editMarker.setImagePath(path + image.getName());
    return image;
  }

  public static EditFragmentMarker instantiate(Marker marker, AuthorViewModel viewModel, EditFragment.UpdateMarkerHandler markerHandler) {
    EditFragmentMarker fragmentMarker = new EditFragmentMarker();
    fragmentMarker.setViewModel(viewModel);
    fragmentMarker.setMarker(marker);
    fragmentMarker.setMarkerHandler(markerHandler);
    return fragmentMarker;
  }
}
