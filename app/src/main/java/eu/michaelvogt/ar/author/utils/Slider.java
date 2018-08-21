package eu.michaelvogt.ar.author.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.Arrays;

import eu.michaelvogt.ar.author.R;

public class Slider extends ViewPager {
  public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    ViewPager mPager = (ViewPager) findViewById(R.id.slider);
    mPager.setAdapter(new SliderAdapter(context, Arrays.asList(R.drawable.ic_collections_black_24dp)));

  }
}
