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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Marker

class MarkerListAdapter(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var markers: List<Marker> = ArrayList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var listener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    fun setMarkers(markers: List<Marker>) {
        this.markers = markers
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.card_header, parent, false)
            HeaderHolder(view)
        } else {
            val view = inflater.inflate(R.layout.card_marker, parent, false)
            MarkerHolder(view)
        }
    }

    override
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = markers[position]
        if (holder is HeaderHolder) {
            holder.titleView.text = item.title
        } else {
            val markerHolder = holder as MarkerHolder
            markerHolder.markerImageView.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                    item.markerImagePath, 100, 100))
            markerHolder.nameView.text = item.title
            markerHolder.infoView.text = item.intro
        }
    }

    override
    fun getItemViewType(position: Int): Int {
        return if (markers[position].isTitle()) TYPE_HEADER else TYPE_ITEM
    }

    override
    fun getItemCount(): Int {
        return markers.size
    }

    internal inner class MarkerHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val markerImageView: ImageView = view.findViewById(R.id.location_image)
        internal val nameView: TextView = view.findViewById(R.id.location_title)
        internal val infoView: TextView = view.findViewById(R.id.marker_info)

        init {
            view.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val uId = markers[position].uId
                        listener!!.onItemClicked(uId)
                    }
                }
            }
        }
    }

    internal inner class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val titleView: TextView = view.findViewById(R.id.header_location)
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}