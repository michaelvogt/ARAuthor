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

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.Renderable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Detail {
  public static final int KEY_IMAGERESOURCE = 14;
  public static final int KEY_IMAGEPATH = 0;
  public static final int KEY_IMAGEFOLDERPATH = 1;
  public static final int KEY_TEXTCONTENT = 2;
  public static final int KEY_MARKUPCONTENT = 3;
  public static final int KEY_TEXTPATH = 4;
  public static final int KEY_SLIDESFOLDERPATH = 5;
  public static final int KEY_FADE_LEFT_WIDTH = 6;
  public static final int KEY_FADE_RIGHT_WIDTH = 7;
  public static final int KEY_FADE_TOP_WIDTH = 8;
  public static final int KEY_FADE_BOTTOM_WIDTH = 9;
  public static final int KEY_BACKGROUNDCOLOR = 10;
  public static final int KEY_TEXTCOLOR = 11;
  public static final int KEY_TEXTSIZE = 12;
  public static final int KEY_ALLOWZOOM = 13;
  public static final int KEY_ISCASTINGSHADOW = 15;
  public static final int KEY_HANDLESEVENT = 16;
  public static final int KEY_EVENTDETAIL = 17;

  public static final String LANGUAGE_EN = "_en_";
  public static final String LANGUAGE_JP = "_jp_";
  public static final String LANGUAGE_DE = "_de_";

  private static final String MATERIAL_FADELEFTWIDTH = "fadeLeftWidth";
  private static final String MATERIAL_FADERIGHTWIDTH = "fadeRightWidth";

  private Map<Integer, Object> details;

  private Function<Integer, Float> value = key -> (float) details.getOrDefault(key, 0.0f);

  private Detail() {
    details = new HashMap<>();
  }

  public static Detail builder() {
    return new Detail();
  }

  public void apply(Material material) {
    material.setFloat(MATERIAL_FADELEFTWIDTH, 0f);
    material.setFloat(MATERIAL_FADERIGHTWIDTH, value.apply(KEY_FADE_RIGHT_WIDTH));
  }

  public void apply(TextView textView) {
    textView.setTextSize((Float) details.getOrDefault(KEY_TEXTSIZE, 12));
    textView.setBackgroundColor((Integer)
        details.getOrDefault(KEY_BACKGROUNDCOLOR, Color.argb(255, 255, 255, 255)));
  }

  public void apply(Renderable renderable) {
    renderable.setShadowCaster((Boolean) details.getOrDefault(KEY_ISCASTINGSHADOW, false));
  }

  public Detail setImageResource(int resourceId) {
    details.put(KEY_IMAGERESOURCE, resourceId);
    return this;
  }

  public Detail setImagePath(@NonNull String path) {
    if (!path.endsWith("jpg") && !path.endsWith(".png"))
      throw new AssertionError("Only .png and .jpg files are supported");

    details.put(KEY_IMAGEPATH, path);
    return this;
  }

  public Detail setImageFolderPath(@NonNull String path) {
    if (path.endsWith("jpg") || path.endsWith(".png"))
      throw new AssertionError("Provide the path to a folder that contains images");

    details.put(KEY_IMAGEFOLDERPATH, path);
    return this;
  }

  public Detail setText(@NonNull String text) {
    if (text.length() <= 0)
      throw new AssertionError("Empty text is not supported");

    details.put(KEY_TEXTCONTENT, text);
    return this;
  }

  public Detail setTextPath(@NonNull String path) {
    if (!path.endsWith(".txt") && !path.endsWith(".html")) throw new AssertionError();

    details.put(KEY_TEXTPATH, path);
    return this;
  }

  public Detail setBackgroundColor(int color) {
    details.put(KEY_BACKGROUNDCOLOR, color);
    return this;
  }

  public Detail setTextColor(int color) {
    details.put(KEY_TEXTCOLOR, color);
    return this;
  }

  public Detail setTextSize(float size) {
    details.put(KEY_TEXTSIZE, size);
    return this;
  }

  public Detail isCastingShadow(boolean isCasting) {
    details.put(KEY_ISCASTINGSHADOW, isCasting);
    return this;
  }

  public Detail setFade(int fadeType, float distance) {
    details.put(fadeType, distance);
    return this;
  }

  public Detail setAllowZoom(boolean allow) {
    details.put(KEY_ALLOWZOOM, allow);
    return this;
  }

  public Detail setHandlesEvent(int eventType, String languageDetail) {
    details.put(KEY_HANDLESEVENT, eventType);
    details.put(KEY_EVENTDETAIL, languageDetail);
    return this;
  }

  public Object getDetail(int key, Object orDefault) {
    // TODO: Prepare general defaults, remove the assertions and use these defaults
    if (!details.containsKey(key)) throw new AssertionError();
    return details.getOrDefault(key, orDefault);
  }

  public Object getDetail(int key) {
    if (!details.containsKey(key)) throw new AssertionError();
    return details.get(key);
  }

  public boolean hasDetail(int key) {
    return details.containsKey(key);
  }
}
