package eu.michaelvogt.ar.author.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Location;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

  private List<Location> locations;
  private LayoutInflater inflater;

  private ItemClickListener listener;

  public LocationListAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  public void setLocations(List<Location> locations) {
    this.locations = locations;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    View v = inflater.inflate(R.layout.card_location, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if (locations == null) {
      holder.locationName.setText("No location");
    } else {
      Location item = locations.get(position);

      holder.locationName.setText(item.getName());
      holder.locationImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
          item.getThumbPath(), 500, 200));
    }
  }

  @Override
  public int getItemCount() {
    return locations == null ? 0 : locations.size();
  }

  public void setItemClickListener(ItemClickListener listener) {
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    private ImageView locationImage;
    private TextView locationName;

    ViewHolder(View view) {
      super(view);

      locationImage = view.findViewById(R.id.location_image);
      locationName = view.findViewById(R.id.location_title);

      view.setOnClickListener(view1 -> {
        if (listener != null) {
          int position = getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            long locationId = locations.get(position).getUId();
            listener.onItemClicked(locationId);
          }
        }
      });
    }
  }

  public Location getLocation(int position) {
    return locations.get(position);
  }
}
