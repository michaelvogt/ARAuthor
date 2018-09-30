package eu.michaelvogt.ar.author.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

public interface BaseDao<T> {
  @Insert
  long insert(T entity);

  @Insert
  void insertAll(T... entity);

  @Update
  void update(T entity);

  @Update
  void updateAll(T... entity);

  @Delete
  void delete(T entity);
}
