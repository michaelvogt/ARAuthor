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

package eu.michaelvogt.ar.author.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Event {
  public static final int NOEVENT = 0;
  public static final int EVENT_SWITCHLANGUAGE = 1;
  public static final int EVENT_GRABCONTENT = 2;
  public static final int EVENT_ZOOMSLIDES = 3;

  private List<Consumer<String>> handlers = new ArrayList<>();

  private final int key;
  private final String data;

  public Event(int key, String data) {
    this.key = key;
    this.data = data;
  }

  public void register(Consumer<String> handler) {
    handlers.add(handler);
  }

  public int getKey() {
    return key;
  }

  public void execute() {
    handlers.forEach(handler -> handler.accept((String) data));

  }
}
