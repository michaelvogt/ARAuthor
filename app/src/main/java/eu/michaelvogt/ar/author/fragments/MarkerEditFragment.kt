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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.data.Marker


class MarkerEditFragment : Fragment() {

    private var editMarkerId: Long = 0
    private lateinit var editMarker: Marker
    private lateinit var viewModel: AuthorViewModel

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_marker_edit, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)
        editMarkerId = viewModel.currentMarkerId
        val cropMarker = viewModel.cropMarker

        if (editMarkerId == -1L) {
            editMarker = Marker()
            if (cropMarker != null) {
                editMarker = cropMarker
                viewModel.clearCropMarker()
            }
            finishSetup()
        } else {
            viewModel.getMarker(editMarkerId)
                    .thenAccept { marker ->
                        editMarker = marker
                        activity!!.runOnUiThread { this.finishSetup() }
                    }
                    .exceptionally { throwable ->
                        Log.e(TAG, "Unable to fetch marker $editMarkerId", throwable)
                        null
                    }
        }
    }

    override
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.actionbar_editmarker_menu, menu)
    }

    override
    fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.actionbar_editmarker_save -> {
                handleSave()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun finishSetup() {
        val tabAdapter = TabAdapter(childFragmentManager, editMarker)
        val tabPager = view!!.findViewById<ViewPager>(R.id.editmarker_pager)
        tabPager.adapter = tabAdapter
    }

    private fun handleAr(view: View) {
        val bundle = Bundle()
        bundle.putString("plane_finding_mode", "VERTICAL")
        bundle.putInt("discovery_controller", 1)
        Navigation.findNavController(view).navigate(R.id.action_marker_preview, bundle)
    }

    private fun handleSave() {
        if (editMarkerId == -1L) {
            editMarker.locationId = viewModel.currentLocationId
            viewModel.addMarker(editMarker)
        } else {
            viewModel.updateMarker(editMarker)
        }

        Navigation.findNavController(view!!).popBackStack()
    }

    private inner class TabAdapter internal constructor(fragmentManager: FragmentManager, editIndex: Marker) : FragmentPagerAdapter(fragmentManager) {
        private val tabTitles = arrayOf("Marker", "Info", "Areas")

        override fun getCount(): Int {
            return NUM_TABS
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MarkerEditFragmentMarker.instantiate(editMarker, viewModel)
                1 -> MarkerEditFragmentInfo.instantiate(editMarker)
                2 -> MarkerEditFragmentAreas.instantiate(editMarkerId)
                else -> throw IllegalArgumentException("Requested edit marker tab doesn't exist: $position")
            }
        }
    }

    companion object {
        private val TAG = MarkerEditFragment::class.java.simpleName
        private const val NUM_TABS = 3
    }
}/* Required empty public constructor*/
