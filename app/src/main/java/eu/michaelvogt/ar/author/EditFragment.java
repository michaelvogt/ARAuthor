package eu.michaelvogt.ar.author;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.ImageView;
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


public class EditFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private int editIndex;
    private TextView editLocation;
    private Marker editMarker;
    private TextView editTitle;
    private ImageView markerImage;
    private AuthorViewModel viewModel;

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

        editTitle = view.findViewById(R.id.edit_title);
        editLocation = view.findViewById(R.id.edit_location);
        markerImage = view.findViewById(R.id.image_marker);

        if (editIndex == -1) {
            editMarker = new Marker();
            if (cropMarker != null) {
                editMarker = cropMarker;
                viewModel.clearCropMarker();
            }
        } else {
            editMarker = viewModel.getMarker(editIndex);
        }

        editTitle.setText(editMarker.getTitle());
        editLocation.setText(editMarker.getLocation());

        if (editMarker.hasImage()) {
            markerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                    editMarker.getImagePath(), 300, 300));
        }

        view.findViewById(R.id.button_save).setOnClickListener(this::handleSave);

        view.findViewById(R.id.button_capture).setOnClickListener(this::handleCapture);

        view.findViewById(R.id.button_import).setOnClickListener(view13 -> handleImport());

        view.findViewById(R.id.button_edit).setOnClickListener(v -> handleCrop(view));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            markerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                   editMarker.getImagePath() , 100, 100));
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
                    markerImage.setImageResource(R.drawable.ic_launcher);
                    e.printStackTrace();
                }

                markerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                        photoPath , 100, 100));

                editMarker.setImagePath(photoPath);
            }
        }
    }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onDetach() { super.onDetach(); }

    private File createImageFile(String filePrefix, String path) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = filePrefix + timeStamp + "_";
        File storageDir = FileUtils.getFullPuplicFolderFile(path);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        editMarker.setImagePath(path + image.getName());
        return image;
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

    private void handleSave(View view) {
        editMarker.setTitle(editTitle.getText().toString());
        editMarker.setLocation(editLocation.getText().toString());

        if (editIndex == -1) {
            viewModel.addMarker(editMarker);
        }

        Navigation.findNavController(view).navigate(R.id.action_list_markers);
    }
}
