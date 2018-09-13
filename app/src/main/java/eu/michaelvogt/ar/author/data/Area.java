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
import android.widget.TextView;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.Renderable;

import java.util.List;
import java.util.Map;

import eu.michaelvogt.ar.author.R;

@Entity(foreignKeys = {
    @ForeignKey(entity = Marker.class,
        parentColumns = "uid",
        childColumns = "marker_id")
})
public class Area {
  public static final int TYPE_DEFAULT = 0;
  public static final int TYPE_3DOBJECTONIMAGE = 1;
  public static final int TYPE_3DOBJECTONPLANE = 2;
  public static final int TYPE_SLIDESONIMAGE = 3;
  public static final int TYPE_INTERACTIVEOVERLAY = 4;
  public static final int TYPE_INTERACTIVEPANEL = 5;
  public static final int TYPE_TEXTONIMAGE = 6;
  public static final int TYPE_IMAGEONIMAGE = 8;
  public static final int TYPE_ROTATIONBUTTON = 9;
  public static final int TYPE_BACKGROUNDONIMAGE = 10;
  public static final int TYPE_COMPARATORONIMAGE = 11;

  public static final int KIND_CONTENT = 0;
  public static final int KIND_UI = 1;

  public static final int COORDINATE_LOCAL = -1;
  public static final int COORDINATE_GLOBAL = -2;

  public static final int ICON_3DOBJECT = R.drawable.ic_account_balance_black_24dp;
  public static final int ICON_FLATOVERLAY = R.drawable.ic_collections_black_24dp;
  public static final int ICON_INTERACTIVEOVERLAY = R.drawable.ic_gamepad_black_24dp;
  public static final int ICON_INTERACTIVEPANEL = R.drawable.ic_my_location_black_24dp;

  public static final String BACKGROUNDAREATITLE = "background";

  @PrimaryKey(autoGenerate = true)
  @NonNull
  private int uid;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "object_type")
  private int objectType;

  @ColumnInfo(name = "usage_type")
  private int usageType;

  @ColumnInfo(name = "coord_type")
  private int coordType;

  @Ignore
  @ColumnInfo(name = "position")
  private Vector3 position;

  @Ignore
  @ColumnInfo(name = "zero_point")
  private Vector3 zeroPoint;

  @ColumnInfo(name = "resource")
  private int resource;

  @Ignore
  @ColumnInfo(name = "detail")
  private Detail detail;

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
    this(0, 0, "", 0, Detail.builder(), Vector3.zero(),
        Vector3.zero(), COORDINATE_LOCAL, Vector3.zero(), Quaternion.identity(), Vector3.one());
  }

  public Area(int opjectType, int usageType, String title, int resource, Detail detail, Vector3 zeroPoint,
              Vector3 size, int coordType, Vector3 position, Quaternion rotation, Vector3 scale) {
    this.objectType = opjectType;
    this.title = title;
    this.usageType = usageType;
    this.size = size;
    this.coordType = coordType;
    this.resource = resource;
    this.detail = valueOrNew(detail);
    this.zeroPoint = zeroPoint;
    this.position = position;
    this.rotation = rotation;
    this.scale = scale;
  }

  public Area(Area area) {
    this.objectType = area.getObjectType();
    this.usageType = area.getUsageType();
    this.title = area.getTitle();
    this.size = area.getSize();
    this.resource = area.getResource();
    this.detail = valueOrNew(area.getDetails());
    this.coordType = area.getCoordType();
    this.zeroPoint = area.getZeroPoint();
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

  public int getUsageType() {
    return usageType;
  }

  public void setUsageType(int usageType) {
    this.usageType = usageType;
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

  public Vector3 getZeroPoint() {
    return zeroPoint;
  }

  public void setZeroPoint(Vector3 zeroPoint) {
    this.zeroPoint = zeroPoint;
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

  public List<Slide> getSlides() {
    return detail.getSlides();
  }


  private Detail getDetails() {
    return detail;
  }

  public Object getDetail(int key, Object orDefault) {
      return detail.getDetail(key, orDefault);
  }

  public Object getDetail(int key) {
      return detail.getDetail(key);
  }

  public String getDetailString(int key) {
    return (String) getDetail(key);
  }

  public String getDetailString(int key, Object orDefault) {
    return (String) getDetail(key, orDefault);
  }

  public int getDetailResource(int key, Object orDefault) {
    return (int) getDetail(key, orDefault);
  }

  public float getDetailFloat(int key, Object orDefault) {
    return (float) getDetail(key, orDefault);
  }

  public int getDetailInt(int key) {
    return (int) getDetail(key);
  }

  public int getDetailInt(int key, Object orDefault) {
    return (int) getDetail(key, orDefault);
  }

  public Vector3 getDetailVector3(int key) {
    return (Vector3) getDetail(key);
  }

  public void setDetail(Detail detail) {
    this.detail = valueOrNew(detail);
  }

  public boolean hasDetail(@NonNull int key) {
    return detail.hasDetail(key);
  }

  public void applyDetail(Material material) {
    detail.apply(material);
  }

  public void applyDetail(TextView textView) {
    detail.apply(textView);
  }

  public void applyDetail(Renderable renderable) {
    detail.apply(renderable);
  }

  public boolean hasDetailEvent() {
    return detail.hasEvents();
  }

  public Map<Integer, EventDetail> getDetailEvents() {
    return detail.getEvents();
  }

  public static Area getDefaultArea(float backgroundHeight, float backgroundWidth) {
    return new Area(TYPE_DEFAULT, KIND_CONTENT, "Default", R.raw.default_model, Detail.builder(),
        Vector3.zero(), null, COORDINATE_LOCAL, new Vector3(-backgroundWidth / 2, 0f, -backgroundHeight / 2),
        new Quaternion(new Vector3(0f, 1f, 0f), 180), Vector3.one());
  }

  public static Area getBackgroundArea(Marker marker, @NonNull String path) {
    Detail detail = Detail.builder().setImagePath(path);

    return new Area(TYPE_BACKGROUNDONIMAGE, KIND_UI, BACKGROUNDAREATITLE, 0, detail, marker.getZeroPoint(), marker.getSize(),
        COORDINATE_LOCAL, new Vector3(0f, 0.01f, 0f), new Quaternion(Vector3.zero(), 0), Vector3.one());
  }

  private Detail valueOrNew(Detail detail) {
    return detail == null ? Detail.builder() : detail;
  }
}
