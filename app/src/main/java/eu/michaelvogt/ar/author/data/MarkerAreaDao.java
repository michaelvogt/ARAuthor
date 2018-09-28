package eu.michaelvogt.ar.author.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MarkerAreaDao {
  @Insert
  void insert(MarkerArea markerArea);

  @Query("SELECT * FROM markers INNER JOIN marker_area ON " +
      "markers.uId=marker_area.markerId WHERE marker_area.areaId=:areaId")
  List<Location> getMarkersForArea(int areaId);

  @Query("SELECT * FROM areas INNER JOIN marker_area ON " +
      "areas.uid = marker_area.areaId WHERE marker_area.markerId =:markerId")
  List<Marker> getAreasForMarker(int markerId);
}
