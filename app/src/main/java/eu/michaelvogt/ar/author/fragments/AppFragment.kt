package eu.michaelvogt.ar.author.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.AuthorViewModel
import eu.michaelvogt.ar.author.utils.Preferences
import kotlinx.android.synthetic.main.activity_author.*
import kotlinx.android.synthetic.main.fragment_locationlist.*

/**
 * Helper functions to set up the elements of the application frame
 *
 * Elements of the application frame are top [Toolbar], [FloatingActionButton], and [BottomAppBar].
 * As a convenience, also provides a [NavController] and [ViewModel].
 */
open class AppFragment : Fragment() {
    lateinit var navController: NavController
    lateinit var viewModel: AuthorViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(activity!!).get(AuthorViewModel::class.java)
    }

    fun setupFab(@DrawableRes iconRes: Int, listener: View.OnClickListener) {
        if (!Preferences.getPreference(context, R.string.allow_edit_pref, false)) {
            hideFab()
            return
        }

        val fab = activity!!.bottom_nav_fab

        (fab as View).visibility = View.VISIBLE
        fab.setImageResource(iconRes)
        fab.setOnClickListener(listener)
    }

    fun hideFab() {
        val fab = activity!!.bottom_nav_fab

        (fab as View).visibility = View.GONE
        fab.setOnClickListener(null)
    }

    fun setupToolbar(@MenuRes toolbarMenuId: Int, listener: Toolbar.OnMenuItemClickListener) {
        top_toolbar.inflateMenu(toolbarMenuId)
        top_toolbar.setOnMenuItemClickListener(listener)
    }

    fun setupBottomNav(@MenuRes actionbarMenuId: Int, listener: Toolbar.OnMenuItemClickListener) {
        with(activity!!.bottom_nav) {
            visibility = View.VISIBLE
            replaceMenu(actionbarMenuId)
            setOnMenuItemClickListener(listener)
        }
    }

    fun showBottomBar() {
        activity!!.bottom_nav.visibility = View.VISIBLE
    }

    fun hideBottomBar() {
        activity!!.bottom_nav.visibility = View.GONE
    }
}