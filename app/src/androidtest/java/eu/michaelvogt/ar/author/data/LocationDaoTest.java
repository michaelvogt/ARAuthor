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

import android.app.Instrumentation;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import eu.michaelvogt.ar.author.data.utils.TestUtil;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LocationDaoTest {
  private AppDatabase db;
  private LocationDao locationDao;

  @Before
  public void createDb() {
    Context context =InstrumentationRegistry.getTargetContext();
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    locationDao = db.locationDao();
  }

  @After
  public void closeDb() throws IOException {
    db.close();
  }

  @Test
  public void get() {
  }

  @Test
  public void getSize() {
  }

  @Test
  public void writeUserAndReadInList() throws Exception {
    Location location = TestUtil.createLocation();
    location.setName("george");
    locationDao.insert(location);
    Location byName = locationDao.findLocationByName("george");
    assertThat(byName, equalTo(location));
  }

}