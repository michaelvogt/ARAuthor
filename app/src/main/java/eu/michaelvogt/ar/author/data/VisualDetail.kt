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

package eu.michaelvogt.ar.author.data

import androidx.room.*
import eu.michaelvogt.ar.author.data.utils.Converters

private val TAG = VisualDetail::class.java.simpleName

const val KEY_IMAGEPATH = 0
const val KEY_IMAGEFOLDERPATH = 1
const val KEY_TEXTCONTENT = 2
const val KEY_MARKUPCONTENT = 3
const val KEY_TEXTPATH = 4
const val KEY_SLIDES = 5
const val KEY_FADE_LEFT_WIDTH = 6
const val KEY_FADE_RIGHT_WIDTH = 7
const val KEY_FADE_TOP_WIDTH = 8
const val KEY_FADE_BOTTOM_WIDTH = 9
const val KEY_BACKGROUNDCOLOR = 10
const val KEY_TEXTCOLOR = 11
const val KEY_TEXTSIZE = 12
const val KEY_ALLOWZOOM = 13
const val KEY_IMAGERESOURCE = 14
const val KEY_ISCASTINGSHADOW = 15
const val KEY_HTMLPATH = 18
const val KEY_ZOOMINSIZE = 19
const val KEY_ZOOMINPOSITION = 20
const val KEY_SECONDARYTEXTURE = 21
const val KEY_SECONDARYIMAGEPATH = 22
const val KEY_TITLE = 23
const val KEY_RESOURCE = 24
const val KEY_LANGUAGE = 25
const val KEY_SCALEVALUES = 26
const val KEY_IMAGEDESCRIPTIONS = 27

const val TYPE_DETAIL_ALL = 0

@Entity(
        tableName = "visual_details",
        indices = [
            Index(value = ["area_id"])
        ])
class VisualDetail(
        @field:ColumnInfo(name = "area_id")
        var areaId: Long = 0,

        @field:ColumnInfo(name = "type")
        var type: Int = 0,

        @field:ColumnInfo(name = "key")
        var key: Int = -1,

        value: Any = "") {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Long = 0

    @ColumnInfo(name = "value")
    protected var value: String = ""

    @Ignore
    var slides: List<Slide> = emptyList()

    var anyValue: Any
        get() = Converters().objectify(value)
        set(anyValue) {
            value = Converters().stringify(anyValue)
        }

    init {
        this.value = Converters().stringify(value)
    }
}
