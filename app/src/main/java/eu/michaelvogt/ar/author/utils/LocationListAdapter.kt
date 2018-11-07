package eu.michaelvogt.ar.author.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.Location

class LocationListAdapter(context: Context?, private val locationMenuHandler: CardMenuHandler) : RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {

    private var locations: List<Location>? = null
    private val inflater: LayoutInflater

    private var listener: ItemClickListener? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    fun setLocations(locations: List<Location>) {
        this.locations = locations
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val v = inflater.inflate(R.layout.card_location, parent, false)
        return ViewHolder(v)
    }

    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (locations == null) {
            holder.locationName.text = "No location"
        } else {
            val item = locations!![position]

            holder.locationName.text = item.name
            holder.locationImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                    item.thumbPath!!, 500, 200))
            holder.locationMenu.setOnClickListener { view -> locationMenuHandler.onMenuClick(view, locations!![position]) }
        }
    }

    override
    fun getItemCount(): Int {
        return if (locations == null) 0 else locations!!.size
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // each data item is just a string in this case
        internal val locationImage: ImageView = view.findViewById(R.id.location_image)
        internal val locationName: TextView = view.findViewById(R.id.location_title)
        internal val locationMenu: ImageView = view.findViewById(R.id.location_menu)

        init {
            view.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val locationId = locations!![position].uId
                        listener!!.onItemClicked(locationId)
                    }
                }
            }
        }
    }

    fun getLocation(position: Int): Location {
        return locations!![position]
    }
}
