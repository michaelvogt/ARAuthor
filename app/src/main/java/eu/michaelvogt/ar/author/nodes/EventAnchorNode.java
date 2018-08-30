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

package eu.michaelvogt.ar.author.nodes;

import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;

import java.util.Map;

public class EventAnchorNode extends AnchorNode {
  private static final String TAG = EventAnchorNode.class.getSimpleName();

  public EventAnchorNode(Anchor anchor) {
    super(anchor);
  }

  @Override
  public boolean onTouchEvent(HitTestResult hitTestResult, MotionEvent motionEvent) {



    return false;
  }
}