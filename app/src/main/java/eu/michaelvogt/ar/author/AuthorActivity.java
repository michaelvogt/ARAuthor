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

package eu.michaelvogt.ar.author;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.CompletableFuture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import eu.michaelvogt.ar.author.data.AppDatabase;

public class AuthorActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_author);

    Toolbar toolbar = findViewById(R.id.toolbar);
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

    AppDatabase database = AppDatabase.Companion.getDatabase(getApplicationContext());

    // Fetching something seems to be the only way to open the database and trigger the callback
    CompletableFuture.supplyAsync(() -> database.locationDao().getSize());
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    for (int i = 0; i < permissions.length; i++) {
      Log.d("Intro Fragment", permissions[i] + " : " + grantResults[i]);

      if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
        hideWhenAvailable(R.id.camera_req_text);
        hideWhenAvailable(R.id.camera_req_btn);
      }

      if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
        hideWhenAvailable(R.id.storage_req_text);
        hideWhenAvailable(R.id.storage_req_btn);
      }
    }
  }

  private void hideWhenAvailable(int viewId) {
    View view = findViewById(viewId);
    if (view != null) {
      view.setVisibility(View.GONE);
    }
  }
}