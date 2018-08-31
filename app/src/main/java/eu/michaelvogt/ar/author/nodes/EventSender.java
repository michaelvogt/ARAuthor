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

public interface EventSender {

  /**
   * Responds if the Node is handling Events at all
   *
   * @return boolean true when handling events, false otherwise
   */
  boolean mayHandleEvent();

  /**
   * Touch event was received at the AnchorNode from this Node
   * <p>
   * Define here which event type should be distributed to the child Nodes of this AnchorNode.
   *
   * @return int  type constant as defined in Event
   */
  int getEventType();

  /**
   * Returns the event detail that was provided when creating this Node.
   *
   * @param eventType int The event type the Detail is needed for
   * @return String the provided Detail
   */
  String getEventDetail(int eventType);
}
