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
import eu.michaelvogt.ar.author.data.tuples.SearchLocation
import eu.michaelvogt.ar.author.databinding.CardLocationHeaderBinding
import eu.michaelvogt.ar.author.databinding.CardLocationSearchBinding
import eu.michaelvogt.ar.author.utils.TextClickListener

class LocationSearchAdapter(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var locations: List<SearchLocation> = emptyList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var listener: TextClickListener? = null

    fun setItemClickListener(listener: TextClickListener) {
        this.listener = listener
    }

    fun setLocations(locations: List<SearchLocation>) {
        this.locations = locations
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LocationSearchAdapter.TYPE_HEADER) {
            val headerBinder = CardLocationHeaderBinding.inflate(inflater, parent, false)
            HeaderHolder(headerBinder)
        } else {
            val locationSearchBinding = CardLocationSearchBinding.inflate(inflater, parent, false)
            LocationSearchHolder(locationSearchBinding)
        }
    }

    override
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val location = locations[position]
        if (holder is HeaderHolder) {       // yes, I know
            holder.binding.location = location
        } else {
            (holder as LocationSearchHolder).binding.location = location
        }
    }


    override
    fun getItemViewType(position: Int): Int {
        return if (locations[position].isTitle) LocationSearchAdapter.TYPE_HEADER else LocationSearchAdapter.TYPE_ITEM
    }

    override
    fun getItemCount(): Int {
        return locations.size
    }

    internal inner class LocationSearchHolder(val binding: CardLocationSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val moduleId = locations[position].moduleId
                        listener!!.onTextClicked(moduleId)
                    }
                }
            }
        }
    }

    internal inner class HeaderHolder(val binding: CardLocationHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}