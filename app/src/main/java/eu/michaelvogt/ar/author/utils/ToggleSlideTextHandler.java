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
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ToggleSlideTextHandler extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
  private GestureDetectorCompat detector;
  private View sliderText;

  public ToggleSlideTextHandler(Context context, View sliderText) {
    this.sliderText = sliderText;

    detector = new GestureDetectorCompat(context, this);
    detector.setOnDoubleTapListener(this);

  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    return detector.onTouchEvent(motionEvent);
  }

  @Override
  public boolean onSingleTapConfirmed(MotionEvent e) {
    sliderText.setVisibility(sliderText.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    return true;
  }

  @Override
  public boolean onDown(MotionEvent e) {
    return true;
  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    return false;
  }
}
