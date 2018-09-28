package eu.michaelvogt.ar.author.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "marker_area",
    primaryKeys = {"markerId", "areaId"},
    foreignKeys = {
        @ForeignKey(entity = Marker.class,
            parentColumns = "id",
            childColumns = "markerId"),
        @ForeignKey(entity = Area.class,
            parentColumns = "id",
            childColumns = "areaId")
    })
public class MarkerArea {
  private int markerId;
  private int areaId;

  public MarkerArea(final int markerId, final int areaId) {
    this.markerId = markerId;
    this.areaId = areaId;
  }

  public int getAreaId() {
    return areaId;
  }

  public void setAreaId(int areaId) {
    this.areaId = areaId;
  }

  public int getMarkerId() {
    return markerId;
  }

  public void setMarkerId(int markerId) {
    this.markerId = markerId;
  }
}
