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
import android.arch.persistence.room.PrimaryKey;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import eu.michaelvogt.ar.author.utils.Converter;

@Entity(tableName = "visual_detail",
    foreignKeys = @ForeignKey(entity = Area.class,
        parentColumns = "u_id",
        childColumns = "area_id"))
public class VisualDetail {
  private static final String TAG = VisualDetail.class.getSimpleName();

  // Default detail
  public static final int KEY_COORDTYPE = 28;
  public static final int KEY_POSITION = 29;
  public static final int KEY_ZEROPOINT = 30;
  public static final int KEY_ROTATION = 32;
  public static final int KEY_SIZE = 33;
  public static final int KEY_SCALE = 34;

  // Optional detail
  public static final int KEY_IMAGEPATH = 0;
  public static final int KEY_IMAGEFOLDERPATH = 1;
  public static final int KEY_TEXTCONTENT = 2;
  public static final int KEY_MARKUPCONTENT = 3;
  public static final int KEY_TEXTPATH = 4;
  public static final int KEY_SLIDES = 5;
  public static final int KEY_FADE_LEFT_WIDTH = 6;
  public static final int KEY_FADE_RIGHT_WIDTH = 7;
  public static final int KEY_FADE_TOP_WIDTH = 8;
  public static final int KEY_FADE_BOTTOM_WIDTH = 9;
  public static final int KEY_BACKGROUNDCOLOR = 10;
  public static final int KEY_TEXTCOLOR = 11;
  public static final int KEY_TEXTSIZE = 12;
  public static final int KEY_ALLOWZOOM = 13;
  public static final int KEY_IMAGERESOURCE = 14;
  public static final int KEY_ISCASTINGSHADOW = 15;
  public static final int KEY_HTMLPATH = 18;
  public static final int KEY_ZOOMINSIZE = 19;
  public static final int KEY_ZOOMINPOSITION = 20;
  public static final int SECONDARYTEXTURE = 21;
  public static final int KEY_SECONDARYIMAGEPATH = 22;
  public static final int KEY_TITLE = 23;
  public static final int KEY_RESOURCE = 24;
  public static final int KEY_LANGUAGE = 25;
  public static final int KEY_SCALEVALUES = 26;
  public static final int KEY_IMAGEDESCRIPTIONS = 27;

  public static final int TYPE_ALL = 0;

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "u_id")
  private int uId;

  @ColumnInfo(name = "area_id")
  private int areaId;

  @ColumnInfo(name = "type")
  private int type;

  @ColumnInfo(name = "target")
  private int target;

  @ColumnInfo(name = "value")
  public String value;

  public VisualDetail() {
    this(0, 0, 0, 0);
  }

  public VisualDetail(int areaId, int type, int target, Object value) {
    this.areaId = areaId;
    this.type = type;
    this.target = target;
    this.value = Converter.stringify(value);
  }

  public int getUId() {
    return uId;
  }

  public void setUId(int uId) {
    this.uId = uId;
  }

  public int getAreaId() {
    return areaId;
  }

  public void setAreaId(int areaId) {
    this.areaId = areaId;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getTarget() {
    return target;
  }

  public void setTarget(int target) {
    this.target = target;
  }

  public Object getValue() {
    return Converter.objectify(value);
  }

  public void setValue(Object value) {
    this.value = Converter.stringify(value);
  }
}
