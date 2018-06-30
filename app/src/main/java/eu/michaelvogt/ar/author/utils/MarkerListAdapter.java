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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class MarkerListAdapter extends RecyclerView.Adapter<MarkerListAdapter.ViewHolder> {
    private final AuthorViewModel mViewModel;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView mMarkerImage;
        private TextView mName;
        private TextView mLocation;

        ViewHolder(View view) {
            super(view);

            mMarkerImage = view.findViewById(R.id.marker_image);
            mName = view.findViewById(R.id.marker_title);
            mLocation = view.findViewById(R.id.marker_location);

            view.setOnClickListener(view1 -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClicked(position);
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MarkerListAdapter(AuthorViewModel viewModel) {
        mViewModel = viewModel;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MarkerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_marker, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Marker item = mViewModel.getMarker(position);
        holder.mMarkerImage.setImageBitmap(ImageUtils.decodeSampledBitmapFromImagePath(
                item.getImagePath(), 100, 100));
        holder.mName.setText(item.getTitle());
        holder.mLocation.setText(item.getLocation());
    }

    @Override
    public int getItemCount() {
        return mViewModel.getMarkerSize();
    }
}