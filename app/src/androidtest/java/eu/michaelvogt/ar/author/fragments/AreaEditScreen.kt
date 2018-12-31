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
import eu.michaelvogt.ar.author.*

class AreaEditScreen : ScopedActions(idMatcher(R.id.area_edit_layout)) {
    fun inToolbar(action: ToolbarActions.() -> Actions) =
            ToolbarActions(idMatcher(R.id.top_areaedit_toolbar)).action()

    fun inDelete(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.actionbar_areaedit_delete)).action()

    fun inCard(index: Int, action: AreaEditCardActions.() -> Actions): Actions =
            AreaEditCardActions(indexMatcher(R.id.areaEditTitle, index)).action()


    companion object {
        const val title = "Edit Area"
        const val input = "input"

        fun open() {
            AreaListScreen.open()
            onView(RecyclerViewMatcher(R.id.area_list).atPosition(1)).perform(click())
        }
    }
}
