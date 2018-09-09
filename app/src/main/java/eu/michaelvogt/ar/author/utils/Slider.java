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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import eu.michaelvogt.ar.author.R;

public class Slider extends ViewPager {
  private Context context;

  private SliderAdapter adapter;

  Function<Integer, Integer> nextItem = currentItem -> (currentItem + 1 >= adapter.getCount()) ? 0 : ++currentItem;

  public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  public void setImages(List<String> images) {
    ViewPager pager = findViewById(R.id.slider);
    adapter = new SliderAdapter(context, images);
    pager.setAdapter(adapter);
  }

  public List<String> getImages() {
    return adapter.getImages();
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
