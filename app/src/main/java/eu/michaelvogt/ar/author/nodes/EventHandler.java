/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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

import com.google.ar.sceneform.Node;

import eu.michaelvogt.ar.author.data.EventDetail;

public interface EventHandler {
  /**
   * Node wants to handle a certain event
   * <p>
   * AnchorNode has received a touch event, which gets then distributed to all Nodes that
   * implement this interface.
   *
   * @param eventDetail   int context detail for the fired event
   * @param motionEvent MotionEvent of the fired event
   */
  void handleEvent(EventDetail eventDetail, MotionEvent motionEvent);

  Iterable<Node> getChildren();
}
