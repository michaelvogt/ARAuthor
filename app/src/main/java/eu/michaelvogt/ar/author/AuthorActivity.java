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
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;

import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AuthorActivity extends AppCompatActivity {

  private AuthorViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    viewModel = ViewModelProviders.of(this).get(AuthorViewModel.class);
    initViewModel();

    setContentView(R.layout.activity_author);
  }

  private void initViewModel() {
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180419_100356.jpg",
        "kumagaike_sign",
        "熊谷家",
        0.648f,
        new ArrayList<>()));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180419_100912.jpg",
        "hidakaya_sign",
        "日高屋",
        0.644f,
        new ArrayList<>()));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180419_125435.jpg",
        "opera_sign",
        "大森座",
        0.671f,
        new ArrayList<>()));
//        viewModel.addMarker(new Marker(
//                "/Touristar/Markers/IMG_20180419_092548.jpg",
//                "kameishi_sign",
//                "城上神社",
//                1.365f));
//        viewModel.addMarker(new Marker(
//                "/Touristar/Markers/IMG_20180419_110133.jpg",
//                "kouengate_sign",
//                "石見公園",
//                0.615f));
//        viewModel.addMarker(new Marker(
//                "/Touristar/Markers/IMG_20180419_101603.jpg",
//                "jiso",
//                "観世音寺",
//                0));
//        viewModel.addMarker(new Marker(
//                "/Touristar/Markers/IMG_20180419_100646.jpg",
//                "aoyamake_sign",
//                "青山家",
//                0.648f));

    viewModel.addMarker(new Marker(
            "/Touristar/Markers/office.png",
            "office_sign",
            "五稜郭",
            0.9f,
            new ArrayList<String>(Arrays.asList(
                "Magistrat Office Building",
                "Magistrat Office Building Infoboard",
                "Cherry blossoms view point"
            ))
        )
    );


    viewModel.addArea(
        new Area(Area.TYPE_3DOBJECT,
            "Magistrat Office Building"),
        new Vector3(),
        new Quaternion());

    viewModel.addArea(
        new Area(Area.TYPE_INTERACTIVEOVERLAY,
            "Magistrat Office Building Infoboard"),
        new Vector3(),
        new Quaternion());

    viewModel.addArea(
        new Area(Area.TYPE_FLATOVERLAY,
            "Cherry blossoms view point"),
        new Vector3(),
        new Quaternion());
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