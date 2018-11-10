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

package eu.michaelvogt.ar.author.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Marker
import eu.michaelvogt.ar.author.databinding.FragmentMarkerEditMarkerBinding
import eu.michaelvogt.ar.author.utils.FileUtils
import eu.michaelvogt.ar.author.utils.ImageUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MarkerEditFragmentMarker : Fragment() {
    private lateinit var editMarker: Marker
    private lateinit var markerImage: ImageView
    private lateinit var viewModel: AuthorViewModel

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        val binder = FragmentMarkerEditMarkerBinding.inflate(inflater, container, false)
        binder.handler = this
        binder.marker = editMarker
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        markerImage = view.findViewById(R.id.image_marker)
        if (editMarker.hasImage()) {
            markerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                    editMarker.markerImagePath, 300, 300))
        }
    }

    private fun setMarker(marker: Marker) {
        this.editMarker = marker
    }

    private fun setViewModel(viewModel: AuthorViewModel) {
        this.viewModel = viewModel
    }

    fun onCropClick(view: View) {
        viewModel.cropMarker = editMarker
        Navigation.findNavController(view).navigate(R.id.action_crop_marker_image)
    }

    fun onImportClick(view: View) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE)
    }


    fun onCaptureClick(view: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile("marker_", FileUtils.MARKERS_PATH)
            } catch (ex: IOException) {
                Log.d("MarkerEditFragment", ex.localizedMessage)
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                activity!!.packageManager
                val photoURI = FileProvider.getUriForFile(context!!,
                        "eu.michaelvogt.ar.author.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(filePrefix: String, path: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = filePrefix + timeStamp + "_"
        val storageDir = FileUtils.getFullPuplicFolderFile(path)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)

        editMarker.markerImagePath = path + image.name
        return image
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            markerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                    editMarker.markerImagePath, 100, 100))
        } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            val selectedImage = data!!.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = activity!!.contentResolver.query(selectedImage!!,
                    filePathColumn, null, null, null)

            if (cursor != null) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val imagePath = cursor.getString(columnIndex)
                cursor.close()

                var photoPath: String

                try {
                    val sourceFile = File(imagePath)
                    photoPath = FileUtils.MARKERS_PATH + sourceFile.name
                    FileUtils.copyFile(sourceFile, FileUtils.getFullPuplicFolderFile(photoPath))
                } catch (e: IOException) {
                    photoPath = ""
                    markerImage.setImageResource(R.drawable.ic_launcher)
                    e.printStackTrace()
                }

                markerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                        photoPath, 100, 100))

                editMarker.markerImagePath = photoPath
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_PICK_IMAGE = 2

        fun instantiate(marker: Marker, viewModel: AuthorViewModel): MarkerEditFragmentMarker {
            val fragmentMarker = MarkerEditFragmentMarker()
            fragmentMarker.setViewModel(viewModel)
            fragmentMarker.setMarker(marker)
            return fragmentMarker
        }
    }
}