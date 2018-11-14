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

package eu.michaelvogt.ar.author.fragments.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.PagerAdapter;
import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Slide;
import eu.michaelvogt.ar.author.data.SlideKt;
import eu.michaelvogt.ar.author.utils.FileUtils;
import eu.michaelvogt.ar.author.utils.NodeCallback;

public class SliderAdapter extends PagerAdapter {
  private static final String TAG = SliderAdapter.class.getSimpleName();

  private final List<Slide> slides;
  private final LayoutInflater inflater;
  private NodeCallback slideCallback;
  private final Context context;

  SliderAdapter(Context context, List<Slide> slides, NodeCallback slideCallback) {
    this.context = context;
    this.slides = slides;
    this.slideCallback = slideCallback;

    inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return slides.size();
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    Object slide;

    int type = slides.get(position).getType();

    if (type == SlideKt.TYPE_IMAGE) {
      slide = instantiateImageSlide(container, position);
    } else if (type == SlideKt.TYPE_VR) {
      slide = instantiateVrSlide(container, position);
    } else if (type == SlideKt.TYPE_COMPARISON) {
      slide = instantiateComparisonSlide(container, position);
    } else {
      throw new IllegalArgumentException("Unknown slide type " + type);
    }

    Log.i(TAG, "Slide successfully created: " + slides.get(position).getContentPath());

    return slide;
  }

  private Object instantiateComparisonSlide(ViewGroup container, int position) {
    View slide = inflater.inflate(R.layout.view_comparison, container, false);

    ImageView imageView = slide.findViewById(R.id.slide_image);
    container.addView(slide, 0);

    Glide.with(container)
        .load(FileUtils.getFullPuplicFolderPath(slides.get(position).getContentPath()))
        .into(imageView);

    ImageButton button = slide.findViewById(R.id.button_compare);
    button.setOnClickListener(view -> slideCallback.createImageComparatorCover());

    return slide;
  }

  @SuppressLint("ClickableViewAccessibility")
  private Object instantiateVrSlide(ViewGroup container, int position) {
    View slide = inflater.inflate(R.layout.view_panorama_image, container, false);

    ImageView imageView = slide.findViewById(R.id.slide_image);
    container.addView(slide, 0);

    Glide.with(container)
        .load(FileUtils.getFullPuplicFolderPath(slides.get(position).getContentPath()))
        .into(imageView);

    AppCompatImageButton button = slide.findViewById(R.id.vr_button);
    button.setOnTouchListener((view, motionEvent) -> true);

    return slide;
  }

  private Object instantiateImageSlide(ViewGroup container, int position) {
    View slide = inflater.inflate(R.layout.view_image, container, false);

    ImageView imageView = slide.findViewById(R.id.slide_image);
    container.addView(slide, 0);

    Glide.with(container)
        .load(FileUtils.getFullPuplicFolderPath(slides.get(position).getContentPath()))
        .into(imageView);

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
