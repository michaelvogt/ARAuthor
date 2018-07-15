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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import eu.michaelvogt.ar.author.R;

@Entity(foreignKeys = {
    @ForeignKey(entity = Marker.class,
              parentColumns = "uid",
              childColumns = "marker_id")
})
public class Area {
  public static final int TYPE_3DOBJECTONIMAGE = 0;
  public static final int TYPE_3DOBJECTONPLANE = 1;
  public static final int TYPE_FLATOVERLAY = 2;
  public static final int TYPE_INTERACTIVEOVERLAY = 3;
  public static final int TYPE_INTERACTIVEPANEL = 4;

  public static final int COORDINATE_LOCAL = -1;
  public static final int COORDINATE_GLOBAL = -2;

  public static final int ICON_3DOBJECT = R.drawable.ic_account_balance_black_24dp;
  public static final int ICON_FLATOVERLAY = R.drawable.ic_collections_black_24dp;
  public static final int ICON_INTERACTIVEOVERLAY = R.drawable.ic_gamepad_black_24dp;
  public static final int ICON_INTERACTIVEPANEL = R.drawable.ic_my_location_black_24dp;

  @PrimaryKey(autoGenerate = true)
  @NonNull
  private int uid;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "object_type")
  private int objectType;

  @ColumnInfo(name = "coord_type")
  private int coordType;

  @Ignore
  @ColumnInfo(name = "position")
  private Vector3 position;

  @ColumnInfo(name = "resource")
  private int resource;

  @Ignore
  @ColumnInfo(name = "size")
  private Vector3 size;

  @Ignore
  @ColumnInfo(name = "rotation")
  private Quaternion rotation;

  @Ignore
  @ColumnInfo(name = "scale")
  private Vector3 scale;

  @ColumnInfo(name = "marker_id")
  private int markerId;

  public Area() {
    this(0, "", 0, Vector3.zero(), COORDINATE_LOCAL, Vector3.zero(), Quaternion.identity(), Vector3.one());
  }

  public Area(int typeResource, String title, int resource, Vector3 size, int coordType, Vector3 location, Quaternion rotation, Vector3 scale) {
    this.objectType = typeResource;
    this.title = title;
    this.size = size;
    this.resource = resource;
    this.position = location;
    this.rotation = rotation;
    this.scale  = scale;
  }

  public Area(Area area) {
    this.objectType = area.getObjectType();
    this.title = area.getTitle();
    this.size = area.getSize();
    this.resource = area.getResource();
    this.coordType = area.getCoordType();
    this.position = area.getPosition();
    this.rotation = area.getRotation();
    this.scale = area.getScale();
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public int getMarkerId() {
    return markerId;
  }

  public void setMarkerId(int markerId) {
    this.markerId = markerId;
  }

  public int getObjectType() {
    return objectType;
  }

  public void setObjectType(int typeResource) {
    this.objectType = typeResource;
  }

  public int getCoordType() {
    return coordType;
  }

  public void setCoordType(int coordType) {
    this.coordType = coordType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }

  public Vector3 getSize() {
    return size;
  }

  public void setSize(Vector3 size) {
    this.size = size;
  }

  public Quaternion getRotation() {
    return rotation;
  }

  public void setRotation(Quaternion rotation) {
    this.rotation = rotation;
  }

  public Vector3 getScale() {
    return scale;
  }

  public void setScale(Vector3 scale) {
    this.scale = scale;
  }

  public int getResource() {
    return resource;
  }

  public void setResource(int resource) {
    this.resource = resource;
  }
}
