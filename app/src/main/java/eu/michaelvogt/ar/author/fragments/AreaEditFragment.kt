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

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.ui.NavigationUI
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AreaVisual
import eu.michaelvogt.ar.author.databinding.FragmentAreaEditBinding
import eu.michaelvogt.ar.author.fragments.support.AreaCardEditHandler
import kotlinx.android.synthetic.main.card_area_edit.view.*
import kotlinx.android.synthetic.main.fragment_area_edit.*

class AreaEditFragment : AppFragment(), AreaCardEditHandler {
    private lateinit var binding: FragmentAreaEditBinding

    private lateinit var useTranslucentSwitch: Switch
    private lateinit var areaVisual: AreaVisual

    private var openedId: Int = 0
    private lateinit var constraintClosed: ConstraintSet
    private lateinit var constraintOpened: ConstraintSet

    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAreaEditBinding.inflate(inflater, container, false)
        binding.handler = this
        return binding.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val areaId = viewModel.currentAreaId
        viewModel.getAreaVisual(areaId)
                .thenAccept { areaVisual ->
                    this.activity!!.runOnUiThread {
                        this.areaVisual = areaVisual
                        binding.area = areaVisual.area
                        binding.notifyChange()
                    }
                }
                .exceptionally { throwable ->
                    Log.e(TAG, "Unable to create area and set data.", throwable)
                    null
                }

        constraintClosed = ConstraintSet()
        constraintClosed.clone(context, R.layout.card_area_edit_close)

        constraintOpened = ConstraintSet()

        NavigationUI.setupWithNavController(top_areaedit_toolbar, navController)
    }

    override
    fun onResume() {
        super.onResume()

        setupFab(android.R.drawable.ic_menu_save, View.OnClickListener {
            // TODO: Save complete AreaVisual
            if (areaVisual.area.uId != 0L) {
                viewModel.updateArea(areaVisual.area).thenAccept {
                    activity!!.runOnUiThread { navController.popBackStack() }
                }
            } else {
                viewModel.insertArea(areaVisual.area).thenAccept {
                    activity!!.runOnUiThread { navController.popBackStack() }
                }
            }
        })

        setupBottomNav(R.menu.actionbar_areaedit_menu, Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.actionbar_areaedit_delete ->
                    viewModel.deleteAreaVisual(areaVisual).thenAccept {
                        activity!!.runOnUiThread { navController.popBackStack() }
                    }.exceptionally { throwable ->
                        Log.e(TAG, "Could not delete Area Visual ${areaVisual.title}", throwable)
                        null
                    }
            }
            true
        })
    }

    override
    fun handleClick(view: View) {
        Log.i(TAG, "click at ${view.resources.getResourceEntryName(view.id)}")

        // TODO: Find the correct transition.
        // With Auto the text field content isn't displayed
        val transition = Slide()
        transition.duration = 300
        transition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                // TODO: Find out why the keyboard isn't opened when focusing the field
                if (openedId != -1) view.area_card_x.requestFocus()
            }
        })

        TransitionManager.beginDelayedTransition(view as ViewGroup, transition)

        if (openedId > 0) {
            val openView = activity?.findViewById<CardView>(openedId)
            constraintClosed.applyTo(openView?.area_card_edit_layout)
            view.area_card_scene.pause()
        }

        if (openedId == view.id) {
            openedId = -1
            hideKeyboard()
        } else {
            constraintOpened.clone(context, R.layout.card_area_edit_open)
            (view as AreaEditCard).adaptForConverter(constraintOpened)

            constraintOpened.applyTo(view.area_card_edit_layout)
            view.area_card_scene.resume()
            view.setAreaPreviewModel()
            openedId = view.id
        }
    }

    private fun handleTest(view: View) {
        val bundle = Bundle()
        bundle.putInt("area_edit_translucency", if (useTranslucentSwitch.isChecked) 1 else 0)
        navController.navigate(R.id.marker_preview_fragment, bundle)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        private val TAG = AreaEditFragment::class.java.simpleName
    }
}
