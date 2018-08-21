package eu.michaelvogt.ar.author.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import eu.michaelvogt.ar.author.R;

public class SliderAdapter extends PagerAdapter {
  private final List<Integer> images;
  private final LayoutInflater inflater;

  public SliderAdapter(Context context, List<Integer> images) {
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
    View slide = inflater.inflate(R.layout.slide, container, false);

    ImageView imageView = (ImageView) slide.findViewById(R.id.slide_image);
    imageView.setImageResource(images.get(position));
    container.addView(slide, 0);

    return slide;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
    return false;
  }
}
