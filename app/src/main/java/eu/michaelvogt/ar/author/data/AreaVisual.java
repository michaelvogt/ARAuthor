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

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.widget.TextView;

import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.Renderable;

import java.util.List;
import java.util.function.Function;

import eu.michaelvogt.ar.author.R;

public class AreaVisual {
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

  // TODO: Get available languages from the database
  public static final String LANGUAGE_EN = "_en_";
  public static final String LANGUAGE_JP = "_jp_";
  public static final String LANGUAGE_DE = "_de_";

  private static final String MATERIAL_FADELEFTWIDTH = "fadeLeftWidth";
  private static final String MATERIAL_FADERIGHTWIDTH = "fadeRightWidth";

  private Area area;
  private SparseArray<VisualDetail> details;
  private SparseArray<EventDetail> events;

  private Function<Integer, Float> value = key -> (float) getDetail(key, 0.0f);

  public AreaVisual(Area area, List<VisualDetail> details, List<EventDetail> events) {
    this.area = area;
    this.details = sparsifyDetails(details);
    this.events = sparsifyEvents(events);
  }

  public AreaVisual(int objectType, int usageType, String title) {
    area = new Area(objectType, usageType, title);
    details = new SparseArray<>();
    events = new SparseArray<>();
  }

  public AreaVisual(AreaVisual areaVisual) {
    this.area = areaVisual.getArea();
    this.details = areaVisual.getDetails();
    this.events = areaVisual.getEvents();
  }


  public void apply(Material material) {
    material.setFloat(MATERIAL_FADELEFTWIDTH, 0f);
    material.setFloat(MATERIAL_FADERIGHTWIDTH, value.apply(VisualDetail.KEY_FADE_RIGHT_WIDTH));
  }

  public void apply(TextView textView) {
    textView.setTextSize((Float) getDetail(VisualDetail.KEY_TEXTSIZE));
    textView.setBackgroundColor((Integer) getDetail(VisualDetail.KEY_BACKGROUNDCOLOR));
  }

  public void apply(Renderable renderable) {
    renderable.setShadowCaster((Boolean) getDetail(VisualDetail.KEY_ISCASTINGSHADOW));
  }

//  public AreaVisual setImagePath(@NonNull String path) {
//    if (!path.endsWith("jpg") && !path.endsWith(".png"))
//      throw new AssertionError("Only .png and .jpg files are supported");
//
//    details.add(new VisualDetail(area.getUid(), VisualDetail.TYPE_ALL, KEY_IMAGEPATH, path));
//    return this;
//  }
//
//  public AreaVisual setSecondaryImagePath(@NonNull String path) {
//    if (!path.endsWith("jpg") && !path.endsWith(".png"))
//      throw new AssertionError("Only .png and .jpg files are supported");
//
//    details.add(new VisualDetail(area.getUid(), VisualDetail.TYPE_ALL, KEY_SECONDARYIMAGEPATH, path));
//    return this;
//  }

  protected Area getArea() {
    return area;
  }

  public SparseArray<VisualDetail> getDetails() {
    return details;
  }

  public SparseArray<EventDetail> getEvents() {
    return events;
  }

  public Object getDetail(int type, Object orDefault) {
    return hasDetail(type) ? details.get(type).getValue() : orDefault;
  }

  public Object getDetail(int type) {
    return details.get(type).getValue();
  }

  public boolean hasDetail(int type) {
    return details.indexOfKey(type) < 0;
  }

  public void applyDetail(Material material) {
//    detail.apply(material);
  }

  public void applyDetail(TextView textView) {
//    detail.apply(textView);
  }

  public void applyDetail(Renderable renderable) {
//    detail.apply(renderable);
  }

  public boolean hasEvent() {
    return events.size() > 0;
  }

  public SparseArray<EventDetail> getDetailEvents() {
    return events.clone();
  }

  private VisualDetail valueOrNew(VisualDetail detail) {
    return detail == null ? new VisualDetail() : detail;
  }

  private SparseArray<VisualDetail> sparsifyDetails(List<VisualDetail> list) {
    SparseArray<VisualDetail> array = new SparseArray<>(list.size());
    list.forEach(visualDetail -> array.put(visualDetail.getType(), visualDetail));
    return array;
  }

  private SparseArray<EventDetail> sparsifyEvents(List<EventDetail> list) {
    SparseArray<EventDetail> array = new SparseArray<>(list.size());
    list.forEach(eventDetail -> array.put(eventDetail.getType(), eventDetail));
    return array;
  }

  public String getTitle() {
    return area.getTitle();
  }

  public int getObjectType() {
    return area.getObjectType();
  }

  public int getUsageType() {
    return area.getUsageType();
  }

  public static AreaVisual getDefaultArea(float backgroundHeight, float backgroundWidth) {
    // TODO: Store Detail
    return new AreaVisual(AreaVisual.TYPE_DEFAULT, AreaVisual.KIND_CONTENT, "Default");
  }

//  R.raw.default_model,
//      Vector3.zero(), null, AreaVisual.COORDINATE_LOCAL, new Vector3(-backgroundWidth / 2, 0f, -backgroundHeight / 2),
//        new Quaternion(new Vector3(0f, 1f, 0f), 180), Vector3.one()

  public static AreaVisual getBackgroundArea(Marker marker, @NonNull String path) {
    // TODO: Store detail
    VisualDetail detail = new VisualDetail(0, VisualDetail.KEY_IMAGEPATH, 0, path);

    return new AreaVisual(AreaVisual.TYPE_BACKGROUNDONIMAGE, AreaVisual.KIND_UI, AreaVisual.BACKGROUNDAREATITLE);
  }

//  0, marker.getZeroPoint(), marker.getSize(),
//  AreaVisual.COORDINATE_LOCAL, new Vector3(0f, 0.1f, 0f), new Quaternion(Vector3.zero(), 0), Vector3.one()

}
