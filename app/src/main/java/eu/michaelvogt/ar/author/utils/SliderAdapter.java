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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import eu.michaelvogt.ar.author.R;

public class SliderAdapter extends PagerAdapter {
  private static final String TAG = SliderAdapter.class.getSimpleName();

  private final List<String> images;
  private final LayoutInflater inflater;

  public SliderAdapter(Context context, List<String> images) {
    this.images = images;
    inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return images.size();
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View slide = inflater.inflate(R.layout.view_slide, container, false);

    ImageView imageView = slide.findViewById(R.id.slide_image);
    container.addView(slide, 0);

    Glide.with(container)
        .load(images.get(position))
        .into(imageView);

    Log.i(TAG, "Slide successfully created: " + images.get(position));

    return slide;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view.equals(object);
  }
}
