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
  public static final int IMAGERESOURCE = 14;
  public static final int IMAGEPATH = 0;
  public static final int IMAGEFOLDERPATH = 1;
  public static final int TEXTCONTENT = 2;
  public static final int MARKUPCONTENT = 3;
  public static final int TEXTPATH = 4;
  public static final int SLIDESFOLDERPATH = 5;
  public static final int FADE_LEFT_WIDTH = 6;
  public static final int FADE_RIGHT_WIDTH = 7;
  public static final int FADE_TOP_WIDTH = 8;
  public static final int FADE_BOTTOM_WIDTH = 9;
  public static final int BACKGROUNDCOLOR = 10;
  public static final int TEXTCOLOR = 11;
  public static final int TEXTSIZE = 12;
  public static final int ALLOWZOOM = 13;
  public static final int ISCASTINGSHADOW = 15;

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
    material.setFloat(MATERIAL_FADERIGHTWIDTH, value.apply(FADE_RIGHT_WIDTH));
  }

  public void apply(TextView textView) {
    textView.setTextSize((Float) details.getOrDefault(TEXTSIZE, 12));
    textView.setBackgroundColor((Integer)
        details.getOrDefault(BACKGROUNDCOLOR, Color.argb(255, 255, 255, 255)));
  }

  public void apply(Renderable renderable) {
    renderable.setShadowCaster((Boolean) details.getOrDefault(ISCASTINGSHADOW, false));
  }

  public Detail setImageResource(int resourceId) {
    details.put(IMAGERESOURCE, resourceId);
    return this;
  }

  public Detail setImagePath(@NonNull String path) {
    if (!path.endsWith("jpg") && !path.endsWith(".png"))
      throw new AssertionError("Only .png and .jpg files are supported");

    details.put(IMAGEPATH, path);
    return this;
  }

  public Detail setImageFolderPath(@NonNull String path) {
    if (path.endsWith("jpg") || path.endsWith(".png"))
      throw new AssertionError("Provide the path to a folder that contains images");

    details.put(IMAGEFOLDERPATH, path);
    return this;
  }

  public Detail setText(@NonNull String text) {
    if (text.length() <= 0)
      throw new AssertionError("Empty text is not supported");

    details.put(TEXTCONTENT, text);
    return this;
  }

  public Detail setTextPath(@NonNull String path) {
    if (!path.endsWith(".txt") && !path.endsWith(".html")) throw new AssertionError();

    details.put(TEXTPATH, path);
    return this;
  }

  public Detail setBackgroundColor(int color) {
    details.put(BACKGROUNDCOLOR, color);
    return this;
  }

  public Detail setTextColor(int color) {
    details.put(TEXTCOLOR, color);
    return this;
  }

  public Detail setTextSize(float size) {
    details.put(TEXTSIZE, size);
    return this;
  }

  public Detail isCastingShadow(boolean isCasting) {
    details.put(ISCASTINGSHADOW, isCasting);
    return this;
  }

  public Detail setFade(int fadeType, float distance) {
    details.put(fadeType, distance);
    return this;
  }

  public Detail setAllowZoom(boolean allow) {
    details.put(ALLOWZOOM, allow);
    return this;
  }

  public Object getDetail(int key, Object orDefault) {
    // TODO: Prepare the defaults, remove the assertions and use the defaults
    if (!details.containsKey(key)) throw new AssertionError();
    return details.getOrDefault(key, orDefault);
  }

  public boolean hasDetail(int key) {
    return details.containsKey(key);
  }
}
