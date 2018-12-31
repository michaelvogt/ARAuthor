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

package eu.michaelvogt.ar.author.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*
import eu.michaelvogt.ar.author.databinding.CardAreaBinding
import eu.michaelvogt.ar.author.utils.ItemClickListener

class AreaListAdapter(context: Context?) : RecyclerView.Adapter<AreaListAdapter.ViewHolder>() {
    private var areas: List<Area> = emptyList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var listener: ItemClickListener? = null

    fun setAreas(areas: List<Area>) {
        this.areas = areas
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder = CardAreaBinding.inflate(inflater, parent, false)
        return ViewHolder(binder)
    }

    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val area = areas[position]
        holder.binder.area = area

        var typeResource = R.drawable.ic_launcher

        when (area.objectType) {
            TYPE_3DOBJECTONPLANE, TYPE_3DOBJECTONIMAGE -> typeResource = ICON_3DOBJECT
            TYPE_SLIDESONIMAGE -> typeResource = ICON_FLATOVERLAY
            TYPE_INTERACTIVEOVERLAY -> typeResource = ICON_INTERACTIVEOVERLAY
        }

        holder.binder.areaImage.setImageResource(typeResource)
    }

    override
    fun getItemCount(): Int = areas.size

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(val binder: CardAreaBinding) : RecyclerView.ViewHolder(binder.root) {
        init {
            binder.root.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val uId = areas[position].uId
                        listener!!.onItemClicked(uId)
                    }
                }
            }
        }
    }
}