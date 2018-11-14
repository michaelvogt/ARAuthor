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
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import eu.michaelvogt.ar.author.R

class AreaEditCard(context: Context, attributes: AttributeSet) : CardView(context, attributes) {
    private var contentLayout: ViewGroup

    init {
        View.inflate(context, R.layout.card_area_edit, this)
        contentLayout = (getChildAt(0) as CardView).getChildAt(0) as ViewGroup

        context.theme
                .obtainStyledAttributes(attributes, R.styleable.AreaEditCard, 0, 0)
                .apply {
                    try {
                        var title = getString(R.styleable.AreaEditCard_title)
                        if (title == null) title = ""
                        setTitle(title)

                        var values = getString(R.styleable.AreaEditCard_values)
                        if (values == null) values = ""
                        setValues(values)
                    } finally {
                        recycle()
                    }
                }
    }

    fun getTitle(): String {
        return (contentLayout.getChildAt(0) as TextView).text.toString()
    }

    fun setTitle(title: String) {
        (contentLayout.getChildAt(0) as TextView).text = title
        invalidate()
        requestLayout()
    }

    fun getValues(): String {
        return (contentLayout.getChildAt(1) as TextView).text.toString()
    }

    fun setValues(values: String) {
        (contentLayout.getChildAt(1) as TextView).text = values
        invalidate()
        requestLayout()
    }
}
