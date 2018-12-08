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

package eu.michaelvogt.ar.author.fragments.support;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Slide;
import eu.michaelvogt.ar.author.fragments.adapters.SliderAdapter;
import eu.michaelvogt.ar.author.utils.NodeCallback;

public class Slider extends ViewPager {
  private static final String TAG = Slider.class.getSimpleName();

  private Context context;

  private SliderAdapter adapter;

  private List<Slide> slides;
  private TextView descriptionView;

  Function<Integer, Integer> nextItem = currentItem -> (currentItem + 1 >= adapter.getCount()) ? 0 : ++currentItem;

  public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  public void setSlides(List<Slide> slides, NodeCallback slideCallback) {
    this.slides = slides;

    descriptionView = ((ViewGroup) getParent()).findViewById(R.id.slider_text);
    descriptionView.setText(slides.get(0).getDescription());

    adapter = new SliderAdapter(context, slides, slideCallback);

    ViewPager pager = findViewById(R.id.slider);
    pager.setAdapter(adapter);
    pager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        descriptionView.setText(slides.get(position).getDescription());
      }
    });
  }

  public List<Slide> getSlides() {
    return slides;
  }

  public void startTimer(int delay) {
    final Handler handler = new Handler();
    final Runnable Update = () -> setCurrentItem(nextItem.apply(getCurrentItem()), true);

    Timer swipeTimer = new Timer();
    swipeTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        handler.post(Update);
      }
    }, delay, delay);
  }
}
