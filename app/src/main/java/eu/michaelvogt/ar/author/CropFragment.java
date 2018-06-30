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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyft.android.scissors.CropView;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class CropFragment extends Fragment {
    private AuthorViewModel viewModel;
    private CropView cropView;
    private String imagePath;

    public CropFragment() {/* Required empty public constructor*/}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
        imagePath = viewModel.getCropMarker().getImagePath();

        Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getFullPuplicFolderPath(imagePath));

        cropView = view.findViewById(R.id.crop_view);
        cropView.setImageBitmap(bitmap);

        view.findViewById(R.id.cropmarker_fab).setOnClickListener(v -> handleCrop(view));
    }

    private void handleCrop(View view) {
        Bitmap croppedBitmap = cropView.crop();
        FileUtils.saveImageToExternalStorage(croppedBitmap, imagePath);

        Navigation.findNavController(view).navigate(R.id.action_edit_marker);
    }
}
