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
package eu.michaelvogt.ar.author.data.utils;

import android.arch.persistence.room.TypeConverter;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import java.util.Date;

public class Converters {
  private static final String DIVIDER = "!!";

  @TypeConverter
  public static Vector3 vector3FromString(String value) {
    if (value == null) return null;

    String[] split = value.split(DIVIDER);
    return new Vector3(
        Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
  }

  @TypeConverter
  public static String vector3ToString(Vector3 vector3) {
    return vector3 == null ? null :
        String.valueOf(vector3.x) + DIVIDER + vector3.y + DIVIDER + vector3.z;
  }


  @TypeConverter
  public static Quaternion quaternionFromString(String value) {
    if (value == null) return null;

    String[] split = value.split(DIVIDER);

    Quaternion result = new Quaternion();
    result.x = Float.parseFloat(split[0]);
    result.y = Float.parseFloat(split[1]);
    result.z = Float.parseFloat(split[2]);
    result.w = Float.parseFloat(split[3]);

    return result;
  }

  @TypeConverter
  public static String quaternionToString(Quaternion quaternion) {
    return quaternion == null ? null : String.valueOf(
        quaternion.x + DIVIDER + quaternion.y + DIVIDER + quaternion.z + DIVIDER + quaternion.w);
  }


  @TypeConverter
  public static Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
  }

  @TypeConverter
  public static Long dateToTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }
}
