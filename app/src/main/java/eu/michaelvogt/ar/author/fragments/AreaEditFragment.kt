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

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Area
import eu.michaelvogt.ar.author.data.AreaVisual
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.databinding.FragmentAreaEditBinding

class AreaEditFragment : Fragment() {
    private lateinit var binding: FragmentAreaEditBinding

    private lateinit var useTranslucentSwitch: Switch
    private lateinit var viewModel: AuthorViewModel
    private lateinit var areaVisual: AreaVisual

    // Temporary objects to hold user edits before button save is pressed
    private lateinit var editArea: Area

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = FragmentAreaEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        val areaId = viewModel.currentAreaId
        viewModel.getAreaVisual(areaId)
                .thenAccept { areaVisual -> this.activity!!.runOnUiThread { finishSetup(view, areaVisual) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to create area and set data.", throwable)
                    null
                }
    }

    override
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.actionbar_areaedit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.actionbar_areaedit_delete -> handleDelete()
            R.id.actionbar_areaedit_save -> handleSave()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun finishSetup(view: View, areaVisual: AreaVisual) {
        this.areaVisual = areaVisual
        this.editArea = Area(areaVisual.area)

//        useTranslucentSwitch = view.findViewById(R.id.area_edit_display_translucent)
//        useTranslucentSwitch.isChecked = arguments!!.getInt("area_edit_translucency") == 1

        binding.area = editArea
        binding.notifyChange()
    }

    private fun handleTest(view: View) {
        val bundle = Bundle()
        bundle.putInt("area_edit_translucency", if (useTranslucentSwitch.isChecked) 1 else 0)
        Navigation.findNavController(view).navigate(R.id.markerPreviewFragment, bundle)
    }

    fun handleSave(): Boolean {
        val area = areaVisual.area
        area.position = editArea.position
        area.rotation = editArea.rotation
        area.scale = editArea.scale
        viewModel.updateArea(area)

        val bundle = Bundle()
        bundle.putInt("area_edit_translucency", if (useTranslucentSwitch.isChecked) 1 else 0)
        Navigation.findNavController(view!!).navigate(R.id.action_edit_marker, bundle)

        return true
    }

    private fun handleDelete(): Boolean {
        // TODO: Implement
        return true
    }

    companion object {
        private val TAG = AreaEditFragment::class.java.simpleName
    }
}
