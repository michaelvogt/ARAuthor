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

import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import eu.michaelvogt.ar.author.*
import eu.michaelvogt.ar.author.List
import org.hamcrest.CoreMatchers.allOf

class AreaListScreen : ScopedActions(idMatcher(R.id.area_list_layout)) {
    fun inToolbar(action: ToolbarActions.() -> Actions) =
            ToolbarActions(idMatcher(R.id.top_toolbar)).action()

    fun inList(action: List.() -> Actions) =
            List(R.id.area_list).action()

    fun inAreaInfo(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.toolbar_area_info)).action()

    fun inInfoPrompt(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.material_target_prompt_view)).action()

    companion object {
        const val title = "Areas"
        const val input = "input"
        const val itemCount = 7

        fun open() {
            onView(allOf(
                    isAssignableFrom(ImageButton::class.java),
                    withParent(isAssignableFrom(Toolbar::class.java)))).perform(click())

            onView(withText(R.string.actionbar_area_list_title)).perform(click())
        }
    }
}
