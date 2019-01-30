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

package eu.michaelvogt.ar.author.data.tuples

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class SearchLocation(
        val name: String,

        val description: String,

        val module_id: String,

        val thumb_path: String,

        val intro_html_path: String,

        @Optional
        val content_size: String = "?KB",

        @Optional
        val is_title: Boolean = false,

        @Optional
        val is_active: Boolean = false
)
