/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.data.Marker
import eu.michaelvogt.ar.author.databinding.FragmentMarkerEditMarkerBinding
import eu.michaelvogt.ar.author.utils.FileUtils
import eu.michaelvogt.ar.author.utils.ImageUtils
import kotlinx.android.synthetic.main.fragment_marker_edit_marker.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment to edit data related to the visual element of the [Marker]
 */
class MarkerEditFragmentMarker : AppFragment() {
    private lateinit var editMarker: Marker
    private lateinit var binder: FragmentMarkerEditMarkerBinding
    private lateinit var capturePhotoPath: String
    private lateinit var locationNames: List<Location>

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        binder = FragmentMarkerEditMarkerBinding.inflate(inflater, container, false)
        binder.handler = this
        binder.marker = editMarker
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (editMarker.hasImage()) {
            val imagePath = editMarker.markerImagePath

            if (imagePath.isNotEmpty() && FileUtils.publicPathExists(imagePath)) {
                val fullPath = FileUtils.getFullPuplicFolderPath(imagePath)
                val bitmap = ImageUtils.decodeSampledBitmapFromImagePath(fullPath, 300, 300)
                image_marker.setImageBitmap(bitmap)
            } else {
                image_marker.setImageResource(R.drawable.ic_launcher)
            }
        }

        viewModel.getLocationNames().thenAccept {
            locationNames = it
        }

        onIsShowBackgroundClick(view)
    }

    fun onCropClick(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.cropMarker = editMarker
        navController.navigate(R.id.action_to_crop_marker_image)
    }

    fun onImportClick(@Suppress("UNUSED_PARAMETER") view: View) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE)
    }


    fun onCaptureClick(@Suppress("UNUSED_PARAMETER") view: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile("marker_", FileUtils.MARKERS_PATH)
            } catch (ex: IOException) {
                Log.d(TAG, "Couldn't create image file", ex)
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                activity!!.packageManager
                val photoURI = FileProvider.getUriForFile(context!!,
                        "eu.michaelvogt.ar.author.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun onIsShowBackgroundClick(view: View) {
        marker_edit_virtual_background.editText?.isEnabled = marker_edit_show_background.isChecked
    }

    @Throws(IOException::class)
    private fun createImageFile(filePrefix: String, path: String): File {
        File(FileUtils.getFullPuplicFolderPath(FileUtils.MARKERS_PATH)).mkdirs()

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPANESE).format(Date())
        val imageFileName = filePrefix + timeStamp + "_"
        val storageDir = FileUtils.getFullPuplicFolderFile(path)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)

        capturePhotoPath = path + image.name
        return image
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val fullPath = FileUtils.getFullPuplicFolderPath(capturePhotoPath)
            // TODO: Trigger scan to add this picture to the gallery
            image_marker.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(fullPath, 100, 100))
            editMarker.markerImagePath = capturePhotoPath
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
                    FileUtils.copyFiles(sourceFile, FileUtils.getFullPuplicFolderFile(photoPath))
                } catch (e: IOException) {
                    photoPath = ""
                    image_marker.setImageResource(R.drawable.ic_launcher)
                    e.printStackTrace()
                }

                val fullPath = FileUtils.getFullPuplicFolderPath(photoPath)
                image_marker.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(fullPath, 100, 100))

                editMarker.markerImagePath = photoPath
            }
        }
        binder.invalidateAll()
    }

    fun onShowLocationPopup(view: View) {
        val popup = PopupMenu(context!!, view)
        locationNames.forEach { popup.menu.add(Menu.NONE, it.uId.toInt(), Menu.NONE, it.name) }

        popup.setOnMenuItemClickListener {
            binder.markerEditMenuLocation.text = it.title
            true
        }
        popup.show()
    }

    fun getSelectLocation(): String {
        return locationNames.find { it.uId == editMarker.locationId }?.name
                ?: resources.getString(R.string.marker_edit_select_location)
    }

    fun setSelectLocation(selectLocation: String) {
        editMarker.locationId = locationNames.find { it.name == selectLocation }?.uId ?: -1
    }

    private fun setMarker(marker: Marker) {
        this.editMarker = marker
    }

    companion object {
        val TAG = MarkerEditFragmentMarker::class.java.simpleName

        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_PICK_IMAGE = 2

        fun instantiate(marker: Marker): MarkerEditFragmentMarker {
            val fragmentMarker = MarkerEditFragmentMarker()
            fragmentMarker.setMarker(marker)
            return fragmentMarker
        }
    }
}