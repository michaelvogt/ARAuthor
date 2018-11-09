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

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.lyft.android.scissors.CropView
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.databinding.FragmentCropBinding
import eu.michaelvogt.ar.author.utils.FileUtils

class CropFragment : Fragment() {
    private var cropView: CropView? = null
    private var imagePath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binder = FragmentCropBinding.inflate(inflater, container, false)
        binder.fragment = this
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)
        imagePath = viewModel.cropMarker!!.markerImagePath

        val bitmap = BitmapFactory.decodeFile(FileUtils.getFullPuplicFolderPath(imagePath))

        cropView = view.findViewById(R.id.crop_view)
        cropView!!.imageBitmap = bitmap
    }

    fun onDoCrop(view: View) {
        val croppedBitmap = cropView!!.crop()
        FileUtils.saveImageToExternalStorage(croppedBitmap!!, imagePath)

        Navigation.findNavController(view).navigate(R.id.action_edit_marker)
    }
}