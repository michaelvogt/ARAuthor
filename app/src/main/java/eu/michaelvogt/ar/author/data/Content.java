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
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Content {
  @PrimaryKey(autoGenerate = true)
  @NonNull
  private Integer uid;

  @Ignore
  @ColumnInfo(name = "type")
  private ContentType type;

  @ColumnInfo(name = "historical_background")
  private String historicalBackground;

  @ColumnInfo(name = "description")
  private String description;

  public Content(String historicalBackground, String description) {
//    this.type = type;
    this.historicalBackground = historicalBackground;
    this.description = description;
  }

  @NonNull
  public Integer getUid() {
    return uid;
  }

  public void setUid(@NonNull Integer uid) {
    this.uid = uid;
  }

  public ContentType getType() {
    return type;
  }

  public void setType(ContentType type) {
    this.type = type;
  }

  public String getHistoricalBackground() {
    return historicalBackground;
  }

  public void setHistoricalBackground(String historicalBackground) {
    this.historicalBackground = historicalBackground;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
