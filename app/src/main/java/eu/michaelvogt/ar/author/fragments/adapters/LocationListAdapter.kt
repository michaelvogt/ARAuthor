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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Location
import eu.michaelvogt.ar.author.databinding.CardLocationBinding
import eu.michaelvogt.ar.author.utils.CardMenuListener
import eu.michaelvogt.ar.author.utils.Preferences

/**
 * Adapter for the list of Locations
 */
class LocationListAdapter(
        private val context: Context?,
        private val listener: CardMenuListener)
    : RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {

    private var locations: List<Location> = emptyList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    fun setLocations(locations: List<Location>) {
        this.locations = locations
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder = CardLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binder)
    }

    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder.location = locations[position]
        holder.binder.handler = this
    }

    override
    fun getItemCount(): Int = locations.size

    fun isEditEnabled(): Int {
        val allowEditPref = Preferences.getPreference(context, R.string.allow_edit_pref, false)
        return if (allowEditPref) View.VISIBLE else View.GONE
    }

    inner class ViewHolder(val binder: CardLocationBinding) : RecyclerView.ViewHolder(binder.root) {
        init {
            binder.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val locationId = locations[adapterPosition].uId
                    listener.onItemClicked(locationId)
                }
            }

            binder.locationMenu.setOnClickListener {
                listener.onMenuClick(it, locations[adapterPosition])
            }
        }
    }
}
