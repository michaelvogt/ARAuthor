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

package eu.michaelvogt.ar.author.data

import androidx.room.*

const val TYPE_IMAGE = 0
const val TYPE_VR = 1
const val TYPE_COMPARISON = 2

@Entity(
        tableName = "slides",
        foreignKeys = [ForeignKey(
                entity = VisualDetail::class,
                parentColumns = arrayOf("u_id"),
                childColumns = arrayOf("detail_id"))
        ],
        indices = [Index(value = ["detail_id"])])
class Slide(
        @ColumnInfo(name = "detail_id")
        var detailId: Long,

        @ColumnInfo(name = "type")
        var type: Int,

        @ColumnInfo(name = "order")
        var order: Int,

        @ColumnInfo(name = "content_path")
        var contentPath: String,

        @ColumnInfo(name = "secondary_paths")
        var secondaryPaths: List<String>?,

        @ColumnInfo(name = "description")
        var description: String?) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0
}
