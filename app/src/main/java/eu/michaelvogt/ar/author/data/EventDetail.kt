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

import android.arch.persistence.room.*
import eu.michaelvogt.ar.author.data.utils.Converters

private val TAG = EventDetail::class.java.simpleName

const val EVENT_SWITCHLANGUAGE = 1
const val EVENT_GRABCONTENT = 2
const val EVENT_ZOOM = 3
const val EVENT_SCALE = 4
const val EVENT_HIDECONTENT = 5
const val EVENT_SETMAINCONTENT = 6

const val TYPE_EVENT_ALL = 0

@Entity(
        tableName = "event_detail",
        indices = [
            Index(value = ["area_id"])
        ])
class EventDetail (
        @field:ColumnInfo(name = "area_id")
        var areaId: Int = 0,

        @field:ColumnInfo(name = "type")
        var type: Int = 0,

        @field:ColumnInfo(name = "target")
        var target: Int = 0,

        value: Any = "") {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    var uId: Int = 0

    @ColumnInfo(name = "value")
    protected var value: String = ""

    var anyValue: Any
        get() = Converters().objectify(value)
        set(anyValue) {
            value = Converters().stringify(anyValue)
        }

    init {
        this.value = Converters().stringify(value)
    }
}
