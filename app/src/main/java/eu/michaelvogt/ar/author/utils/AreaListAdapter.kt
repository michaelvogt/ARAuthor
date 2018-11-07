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

package eu.michaelvogt.ar.author.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*

class AreaListAdapter : RecyclerView.Adapter<AreaListAdapter.ViewHolder>() {
    private var listener: ItemClickListener? = null
    private var areas: List<Area>? = null

    fun setAreas(areas: List<Area>) {
        this.areas = areas
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_area, parent, false)
        return ViewHolder(v)
    }

    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (areas != null) {
            val area = areas!![position]
            holder.areaName.text = area.title

            var typeResource = R.drawable.ic_launcher

            when (area.objectType) {
                TYPE_3DOBJECTONPLANE, TYPE_3DOBJECTONIMAGE -> typeResource = ICON_3DOBJECT
                TYPE_SLIDESONIMAGE -> typeResource = ICON_FLATOVERLAY
                TYPE_INTERACTIVEOVERLAY -> typeResource = ICON_INTERACTIVEOVERLAY
            }

            holder.areaImage.setImageResource(typeResource)
        }
    }

    override
    fun getItemCount(): Int {
        return if (areas == null) 0 else areas!!.size
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // each data item is just a string in this case
        internal val areaImage: ImageView
        internal val areaName: TextView

        init {

            areaImage = view.findViewById(R.id.area_image)
            areaName = view.findViewById(R.id.area_title)

            view.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val uId = areas!![position].uId
                        listener!!.onItemClicked(uId)
                    }
                }
            }
        }
    }
}