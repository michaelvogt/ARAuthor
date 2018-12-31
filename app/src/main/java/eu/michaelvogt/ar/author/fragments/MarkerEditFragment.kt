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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Marker
import kotlinx.android.synthetic.main.fragment_marker_edit.*
import java.util.concurrent.CompletableFuture


/**
 * Fragment to edit a [Marker]
 */
class MarkerEditFragment : AppFragment() {
    private var editMarkerId: Long = 0
    private lateinit var editMarker: Marker

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_marker_edit, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editMarkerId = viewModel.currentMarkerId
        val cropMarker = viewModel.cropMarker

        if (editMarkerId == -1L) {
            editMarker = Marker()
            editMarker.locationId = viewModel.currentLocationId

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

        NavigationUI.setupWithNavController(top_toolbar, navController)
    }

    override fun onResume() {
        super.onResume()

        setupFab(android.R.drawable.ic_menu_save, View.OnClickListener {
            // TODO: Decide on correct validation strategy
            if (editMarker.locationId <= 0) {
                Snackbar.make(view!!, "@string/marker_edit_error_select_location", Snackbar.LENGTH_SHORT).show()
                return@OnClickListener
            }

            val future: CompletableFuture<*>
            if (editMarkerId == -1L) {
                future = viewModel.insertMarker(editMarker)
            } else {
                future = viewModel.updateMarker(editMarker)
            }

            future.thenAccept {
                activity!!.runOnUiThread { navController.popBackStack() }
            }.exceptionally {
                Log.e(TAG, "Unable to insert/update marker $editMarkerId", it)
                null
            }
        })

        setupBottomNav(R.menu.actionbar_markeredit_menu, Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.actionbar_markeredit_delete -> {
                    viewModel.deleteMarker(editMarker)
                            .thenAccept { activity!!.runOnUiThread { navController.popBackStack() } }
                    true
                }
                else -> false
            }
        })
    }

    private fun finishSetup() {
        val tabAdapter = TabAdapter(childFragmentManager)
        val tabPager = view!!.findViewById<ViewPager>(R.id.editmarker_pager)
        tabPager.adapter = tabAdapter
    }

//    private fun handleAr(view: View) {
//        val bundle = Bundle()
//        bundle.putString("plane_finding_mode", "VERTICAL")
//        bundle.putInt("discovery_controller", 1)
//        navController.navigate(R.id.markerPreviewFragment, bundle)
//    }

    private inner class TabAdapter internal constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        private val tabTitles = arrayOf("Marker", "Info", "Areas")

        override fun getCount(): Int {
            return NUM_TABS
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MarkerEditFragmentMarker.instantiate(editMarker)
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
