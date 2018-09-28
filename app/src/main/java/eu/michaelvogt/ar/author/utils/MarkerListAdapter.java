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
import eu.michaelvogt.ar.author.data.Marker;

public class MarkerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final static int TYPE_HEADER = 0;
  private final static int TYPE_ITEM = 1;

  private List<Marker> markers;
  private LayoutInflater inflater;

  private ItemClickListener listener;

  public void setItemClickListener(ItemClickListener listener) {
    this.listener = listener;
  }

  public MarkerListAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  public void setMarkers(List<Marker> markers) {
    this.markers = markers;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == TYPE_HEADER) {
      View view = inflater.inflate(R.layout.card_header, parent, false);
      return new HeaderHolder(view);
    } else {
      View view = inflater.inflate(R.layout.card_marker, parent, false);
      return new MarkerHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    Marker item = markers.get(position);

    if (holder instanceof HeaderHolder) {
      HeaderHolder headerHolder = (HeaderHolder) holder;
      headerHolder.titleView.setText(item.getTitle());
    } else {
      MarkerHolder markerHolder = (MarkerHolder) holder;
      markerHolder.markerImageView.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
          item.getMarkerImagePath(), 100, 100));
      markerHolder.nameView.setText(item.getTitle());
      markerHolder.infoView.setText(item.getIntro());
    }
  }

  @Override
  public int getItemViewType(int position) {
    return markers.get(position).isTitle() ? TYPE_HEADER : TYPE_ITEM;
  }

  @Override
  public int getItemCount() {
    return markers.size();
  }

  class MarkerHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    private ImageView markerImageView;
    private TextView nameView;
    private TextView infoView;

    MarkerHolder(View view) {
      super(view);

      markerImageView = view.findViewById(R.id.location_image);
      nameView = view.findViewById(R.id.location_title);
      infoView = view.findViewById(R.id.marker_info);

      view.setOnClickListener(view1 -> {
        if (listener != null) {
          int position = getAdapterPosition();
          if (position != RecyclerView.NO_POSITION) {
            listener.onItemClicked(position);
          }
        }
      });
    }
  }

  class HeaderHolder extends RecyclerView.ViewHolder {
    private TextView titleView;

    HeaderHolder(View view) {
      super(view);

      titleView = view.findViewById(R.id.header_location);
    }
  }
}