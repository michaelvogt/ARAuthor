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

import eu.michaelvogt.ar.author.*
import eu.michaelvogt.ar.author.List

class LocationListScreen : ScopedActions(idMatcher(R.id.locationlist_layout)) {
    fun inToolbar(action: ToolbarActions.() -> Actions) =
            ToolbarActions(idMatcher(R.id.top_toolbar)).action()

    fun inList(action: List.() -> Actions) =
            List(R.id.location_list).action()

    fun inLocationInfo(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.toolbar_location_info)).action()

    fun inInfoPrompt(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.material_target_prompt_view)).action()

    fun inEditMenu(action: UiActions.() -> Actions) =
            UiActions(textMatcher(editLocationMenu)).action()

    companion object {
        const val title = "Locations"
        const val itemCount = 3
        const val editLocationMenu = "Edit"
    }
}
