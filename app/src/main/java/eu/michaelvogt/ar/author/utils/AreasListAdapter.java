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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;

public class AreasListAdapter extends RecyclerView.Adapter<AreasListAdapter.ViewHolder> {
  private final AuthorViewModel viewModel;
  private OnItemClickListener listener;

  public interface OnItemClickListener {
    void onItemClicked(int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    private ImageView areaImage;
    private TextView areaName;

    ViewHolder(View view) {
      super(view);

      areaImage = view.findViewById(R.id.area_image);
      areaName = view.findViewById(R.id.area_title);

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

  // Provide a suitable constructor (depends on the kind of dataset)
  public AreasListAdapter(AuthorViewModel viewModel) {
    this.viewModel = viewModel;
  }

  // Create new views (invoked by the layout manager)
  @NonNull
  @Override
  public AreasListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.card_area, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Area item = viewModel.getArea(position);
    holder.areaName.setText(item.getTitle());

    int typeResource = R.drawable.ic_launcher;

    switch (item.getObjectType()) {
      case Area.TYPE_3DOBJECT:
        typeResource = Area.ICON_3DOBJECT;
        break;
      case Area.TYPE_FLATOVERLAY:
        typeResource = Area.ICON_FLATOVERLAY;
        break;
      case Area.TYPE_INTERACTIVEOVERLAY:
        typeResource = Area.ICON_INTERACTIVEOVERLAY;
        break;
    }

    holder.areaImage.setImageResource(typeResource);
  }

  @Override
  public int getItemCount() {
    return viewModel.getAreaSize();
  }
}