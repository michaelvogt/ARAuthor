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

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import eu.michaelvogt.ar.author.R;

public class Area {
  public static final int TYPE_3DOBJECT = 0;
  public static final int TYPE_FLATOVERLAY = 1;
  public static final int TYPE_INTERACTIVEOVERLAY = 3;

  public static final int ICON_3DOBJECT = R.drawable.ic_account_balance_black_24dp;
  public static final int ICON_FLATOVERLAY = R.drawable.ic_collections_black_24dp;
  public static final int ICON_INTERACTIVEOVERLAY = R.drawable.ic_gamepad_black_24dp;

  private String title;
  private int type;
  private Vector3 location;
  private Quaternion rotation;

  public Area() {
    this(0, "");
  }

  public Area(int typeResource, String title) {
    this.type = typeResource;
    this.title = title;
  }

  public Area(Area area) {
    this.type = area.getType();
    this.title = area.getTitle();
  }

  public int getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public void setType(int typeResource) {
    this.type = typeResource;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Vector3 getLocation() {
    return location;
  }

  public void setLocation(Vector3 location) {
    this.location = location;
  }

  public Quaternion getRotation() {
    return rotation;
  }

  public void setRotation(Quaternion rotation) {
    this.rotation = rotation;
  }
}
