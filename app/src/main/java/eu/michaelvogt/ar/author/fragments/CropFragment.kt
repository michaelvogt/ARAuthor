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

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.michaelvogt.ar.author.databinding.FragmentCropBinding
import eu.michaelvogt.ar.author.utils.FileUtils
import kotlinx.android.synthetic.main.fragment_crop.*

class CropFragment : AppFragment() {
    private var imagePath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binder = FragmentCropBinding.inflate(inflater, container, false)
        binder.handler = this
        return binder.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagePath = viewModel.cropMarker!!.markerImagePath
        crop_view.imageBitmap = BitmapFactory.decodeFile(FileUtils.getFullPuplicFolderPath(imagePath))
    }

    override
    fun onResume() {
        super.onResume()

        setupFab(android.R.drawable.ic_menu_save, Companion.FabVisibility.EDITING, View.OnClickListener {
            FileUtils.saveImageToExternalStorage(crop_view.crop()!!, imagePath)
            navController.popBackStack()
        })

        hideBottomBar()
    }
}