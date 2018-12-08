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
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.utils.DisplayStringFunctions
import kotlinx.android.synthetic.main.card_area_edit.view.*

class AreaEditCard(context: Context, attributes: AttributeSet) : CardView(context, attributes) {
    private var converterName: String?
    private var converter: DisplayStringFunctions? = null
    private var previewNode: Node? = null

    var onValuesChangeListener: AreaEditCard.OnValuesChangeListener? = null

    init {
        View.inflate(context, R.layout.card_area_edit, this)

        context.theme
                .obtainStyledAttributes(attributes, R.styleable.AreaEditCard, 0, 0)
                .apply {
                    try {
                        setTitle(getString(R.styleable.AreaEditCard_title) ?: "")
                        setValue(getString(R.styleable.AreaEditCard_values) ?: "")

                        converterName = getString(R.styleable.AreaEditCard_converter)
                        converter = DisplayStringFunctions(this@AreaEditCard, converterName)
                    } finally {
                        recycle()
                    }
                }

        area_card_x.editText?.setOnFocusChangeListener { view: View, isFocused: Boolean ->
            handleFocus(view, isFocused)
        }

        area_card_y.editText?.setOnFocusChangeListener { view: View, isFocused: Boolean ->
            handleFocus(view, isFocused)
        }

        area_card_z.editText?.setOnFocusChangeListener { view: View, isFocused: Boolean ->
            handleFocus(view, isFocused)
        }

        area_card_w.editText?.setOnFocusChangeListener { view: View, isFocused: Boolean ->
            handleFocus(view, isFocused)
        }

        area_card_s.setOnCheckedChangeListener { group, checkedId ->
            handleChecked(group, checkedId)
        }

        area_card_accept.setOnClickListener {
            (area_card.parent as View).performClick()
        }

        area_card_scene.setOnClickListener {
            // TODO: Implement handler to move area by gesture, prevent list scroll
        }

        area_card.setOnClickListener {
            // TODO: setup input elements for converter type
            (area_card.parent as View).performClick()
        }
    }

    fun adaptForConverter(constraint: ConstraintSet) {
        when (converterName) {
            resources.getString(R.string.display_converter_from_quaternion) -> {
                area_card_x.editText?.hint = resources.getString(R.string.area_card_labelx)
                area_card_x.editText?.inputType = InputType.TYPE_CLASS_NUMBER +
                        InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            resources.getString(R.string.display_converter_from_vector3) -> {
                area_card_x.editText?.hint = resources.getString(R.string.area_card_labelx)
                area_card_x.editText?.inputType = InputType.TYPE_CLASS_NUMBER +
                        InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED
                constraint.setVisibility(R.id.area_card_w, View.GONE)
            }
            resources.getString(R.string.display_converter_from_selection) -> {
                constraint.setVisibility(R.id.area_card_x, View.GONE)
                constraint.setVisibility(R.id.area_card_y, View.GONE)
                constraint.setVisibility(R.id.area_card_z, View.GONE)
                constraint.setVisibility(R.id.area_card_w, View.GONE)
                constraint.setVisibility(R.id.area_card_s, View.VISIBLE)
            }
            else -> {
                area_card_x.editText?.inputType = InputType.TYPE_CLASS_TEXT
                area_card_x.hint = "Value"
                constraint.setVisibility(R.id.area_card_y, View.GONE)
                constraint.setVisibility(R.id.area_card_z, View.GONE)
                constraint.setVisibility(R.id.area_card_w, View.GONE)
            }
        }
    }

    fun getTitle(): String {
        return area_card_title.text.toString()
    }

    fun setTitle(value: String) {
        area_card_title.text = value
        invalidate()
        requestLayout()
    }

    fun getValue(): Any {
        return area_card_values.text.toString()
    }

    fun setValue(value: String) {
        area_card_values.text = value
        onValuesChangeListener?.onValuesChanged(this)
        invalidate()
        requestLayout()
    }

    fun getXValue(): String {
        return area_card_x.editText?.text.toString()
    }

    fun setXValue(value: String) {
        area_card_x.editText?.text?.clear()
        area_card_x.editText?.text?.insert(0, value)
        invalidate()
        requestLayout()
    }

    fun getYValue(): String {
        return area_card_y.editText?.text.toString()
    }

    fun setYValue(value: String) {
        area_card_y.editText?.text?.clear()
        area_card_y.editText?.text?.insert(0, value)
        invalidate()
        requestLayout()
    }

    fun getZValue(): String {
        return area_card_z.editText?.text.toString()
    }

    fun setZValue(value: String) {
        area_card_z.editText?.text?.clear()
        area_card_z.editText?.text?.insert(0, value)
        invalidate()
        requestLayout()
    }

    fun getWValue(): String {
        return area_card_w.editText?.text.toString()
    }

    fun setWValue(value: String) {
        area_card_w.editText?.text?.clear()
        area_card_w.editText?.text?.insert(0, value)
        invalidate()
        requestLayout()
    }

    fun getSValue(): String {
        val id = area_card_s.checkedRadioButtonId
        val child = area_card_s.getChildAt(id) as RadioButton
        return child.text.toString()
    }

    fun getSValueId(): Int {
        return (area_card_s as RadioGroup).checkedRadioButtonId
    }

    fun setSValue(id: Int, value: SparseArray<String>) {
        area_card_s.removeAllViews()
        value.forEach { index, title ->
            val button = RadioButton(context)
            button.text = title
            button.id = index
            button.isChecked = id == index
            area_card_s.addView(button)
        }

        invalidate()
        requestLayout()
    }

    fun setAreaPreviewModel() {
        previewNode?.setParent(null)
        previewNode = Node()

        // reference area
        MaterialFactory.makeOpaqueWithColor(context, Color(1.0f, 1f, 1f))
                .thenAccept {
                    with(previewNode!!) {
                        renderable = ShapeFactory.makeCube(Vector3(3f, .7f, .01f), Vector3.zero(), it)
                        localPosition = Vector3(0f, 0f, -.5f)
                        localRotation = Quaternion(Vector3(1f, 0f, 0f), -20f)
                        setParent(area_card_scene.scene)
                    }
                }.exceptionally {
                    Log.e(TAG, "Area node couldn't be created", it)
                    null

                }

        MaterialFactory.makeOpaqueWithColor(context, Color(1.0f, 1f, 0f))
                .thenAccept {
                    Node().apply {
                        renderable = ShapeFactory.makeCube(Vector3(.8f, .5f, .01f), Vector3.zero(), it)
                        localPosition = Vector3(.8f, -.03f, .01f)
                        setParent(previewNode)
                    }
                }
    }

    private fun handleChecked(group: RadioGroup, checkedId: Int) {
        for (index in 0 until group.childCount) {
            if (group.getChildAt(index).id == checkedId) {
                setValue((group.getChildAt(index) as RadioButton).text.toString())
            }
        }
    }

    private fun handleFocus(field: View, isFocused: Boolean) {
        if (field is EditText && isAttachedToWindow && !isFocused)
            setValue(converter?.accept() ?: field.text.toString())
    }

    interface OnValuesChangeListener {
        fun onValuesChanged(areaEditCard: AreaEditCard)
    }

    companion object {
        private val TAG = AreaEditCard::class.java.simpleName
    }
}
