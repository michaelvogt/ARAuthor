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

package eu.michaelvogt.ar.author

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import eu.michaelvogt.ar.author.data.AreaVisual
import eu.michaelvogt.ar.author.data.AuthorViewModel

class AreaEditFragment : Fragment() {

    private lateinit var useTranslucentSwitch: Switch

    private lateinit var locationX: EditText
    private lateinit var locationY: EditText
    private lateinit var locationZ: EditText
    private lateinit var rotationX: EditText
    private lateinit var rotationY: EditText
    private lateinit var rotationZ: EditText
    private lateinit var rotationW: EditText
    private lateinit var scaleX: EditText
    private lateinit var scaleY: EditText
    private lateinit var scaleZ: EditText

    private var viewModel: AuthorViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_area_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)

        val areaId = viewModel!!.currentAreaId
        viewModel!!.getAreaVisual(areaId)
                .thenAccept { areaVisual -> this.activity!!.runOnUiThread { finishSetup(view, areaVisual) } }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to create area and set data.", throwable)
                    null
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.actionbar_areaedit_menu, menu)
    }

    private fun finishSetup(view: View, editArea: AreaVisual) {
        useTranslucentSwitch = view.findViewById(R.id.area_edit_display_translucent)
        useTranslucentSwitch.isChecked = arguments!!.getInt("area_edit_translucency") == 1

        locationX = view.findViewById(R.id.areaedit_xl_edit)
        locationX.setText(editArea.position.x.toString())

        locationY = view.findViewById(R.id.areaedit_yl_edit)
        locationY.setText(editArea.position.y.toString())

        locationZ = view.findViewById(R.id.areaedit_zl_edit)
        locationZ.setText(editArea.position.z.toString())

        rotationX = view.findViewById(R.id.areaedit_xr_edit)
        rotationX.setText(editArea.rotation.x.toString())

        rotationY = view.findViewById(R.id.areaedit_yr_edit)
        rotationY.setText(editArea.rotation.y.toString())

        rotationZ = view.findViewById(R.id.areaedit_zr_edit)
        rotationZ.setText(editArea.rotation.z.toString())

        rotationW = view.findViewById(R.id.areaedit_wr_edit)
        rotationW.setText(editArea.rotation.w.toString())

        scaleX = view.findViewById(R.id.areaedit_xs_edit)
        scaleX.setText(editArea.scale.x.toString())

        scaleY = view.findViewById(R.id.areaedit_ys_edit)
        scaleY.setText(editArea.scale.y.toString())

        scaleZ = view.findViewById(R.id.areaedit_zs_edit)
        scaleZ.setText(editArea.scale.z.toString())

        view.findViewById<View>(R.id.areaedit_save).setOnClickListener { v -> handleSave(v, editArea) }
        view.findViewById<View>(R.id.areaedit_cancel).setOnClickListener { this.handleCancel(it) }
        view.findViewById<View>(R.id.areaedit_test).setOnClickListener { this.handleTest(it) }
    }

    private fun handleCancel(view: View) {
        val bundle = Bundle()
        bundle.putInt("area_edit_translucency", if (useTranslucentSwitch.isChecked) 1 else 0)
        Navigation.findNavController(view).navigate(R.id.action_edit_marker, bundle)
    }

    private fun handleTest(view: View) {
        val bundle = Bundle()
        bundle.putInt("area_edit_translucency", if (useTranslucentSwitch.isChecked) 1 else 0)
        Navigation.findNavController(view).navigate(R.id.action_test_area_placement, bundle)
    }

    private fun handleSave(view: View, editArea: AreaVisual) {
        val location = Vector3(asFloat(locationX), asFloat(locationY), asFloat(locationZ))
        val rotation = Quaternion(
                asFloat(rotationX), asFloat(rotationY), asFloat(rotationZ), asFloat(rotationW))
        val scale = Vector3(asFloat(scaleX), asFloat(scaleY), asFloat(scaleZ))

        val area = editArea.area
        area.position = location
        area.rotation = rotation
        area.scale = scale
        viewModel!!.updateArea(area)

        val bundle = Bundle()
        bundle.putInt("area_edit_translucency", if (useTranslucentSwitch.isChecked) 1 else 0)
        Navigation.findNavController(view).navigate(R.id.action_edit_marker, bundle)
    }

    private fun asFloat(text: EditText): Float {
        return java.lang.Float.parseFloat(text.text.toString())
    }

    companion object {
        private val TAG = AreaEditFragment::class.java.simpleName
    }
}
