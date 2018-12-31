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

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import eu.michaelvogt.ar.author.*
import org.hamcrest.CoreMatchers.allOf

class LocationEditScreen : ScopedActions(idMatcher(R.id.location_edit_layout)) {
    fun inToolbar(action: ToolbarActions.() -> Actions) =
            ToolbarActions(idMatcher(R.id.top_toolbar)).action()

    fun inDelete(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.actionbar_location_delete)).action()

    fun inNameLayout(action: TextInputLayoutActions.() -> Actions) =
            TextInputLayoutActions(R.id.location_edit_name).action()

    fun inDescLayout(action: TextInputLayoutActions.() -> Actions) =
            TextInputLayoutActions(R.id.location_edit_desc).action()

    fun inThumbLayout(action: TextInputLayoutActions.() -> Actions) =
            TextInputLayoutActions(R.id.location_edit_thumb).action()

    fun inIntroLayout(action: TextInputLayoutActions.() -> Actions) =
            TextInputLayoutActions(R.id.location_edit_intro).action()

    companion object {
        const val title = "Edit Location"
        const val input = "input"

        fun open() {
            onView(allOf(withId(R.id.location_menu),
                    hasSibling(withSubstring("石見銀山")))).perform(click())

            onView(withText(R.string.menu_location_edit)).perform(click())
        }
    }
}
