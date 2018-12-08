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

package eu.michaelvogt.ar.author.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.michaelvogt.ar.author.data.tuples.ListMarker
import eu.michaelvogt.ar.author.databinding.CardHeaderBinding
import eu.michaelvogt.ar.author.databinding.CardMarkerBinding
import eu.michaelvogt.ar.author.utils.ItemClickListener

class MarkerListAdapter(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var markers: List<ListMarker> = emptyList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var listener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    fun setMarkers(markers: List<ListMarker>) {
        this.markers = markers
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val headerBinder = CardHeaderBinding.inflate(inflater, parent, false)
            HeaderHolder(headerBinder)
        } else {
            val markerBinder = CardMarkerBinding.inflate(inflater, parent, false)
            MarkerHolder(markerBinder)
        }
    }

    override
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val marker = markers[position]
        if (holder is HeaderHolder) {       // yes, I know
            holder.binding.marker = marker
        } else {
            (holder as MarkerHolder).binding.marker = marker
        }
    }

    override
    fun getItemViewType(position: Int): Int {
        return if (markers[position].isTitle == true) TYPE_HEADER else TYPE_ITEM
    }

    override
    fun getItemCount(): Int {
        return markers.size
    }

    internal inner class MarkerHolder(val binding: CardMarkerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val uId = markers[position].markerId
                        listener!!.onItemClicked(uId)
                    }
                }
            }
        }
    }

    internal inner class HeaderHolder(val binding: CardHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}