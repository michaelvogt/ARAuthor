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

import java.util.Arrays;

import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Location;

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
    viewModel.addLocation(new Location(
        "石見銀山",
        "Touristar/iwamiginzan/images/igk_machinami.jpg",
        "Touristar/iwamiginzan/intro.html",
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)));

    viewModel.addLocation(new Location(
        "Hakodate",
        "Touristar/hakodate/images/goryokakumainhall.jpg",
        "Touristar/hakodate/intro.html",
        Arrays.asList(29, 30)));

 /*
      viewModel.addArea(new Area(Area.TYPE_IMAGEONIMAGE,
        Area.KIND_CONTENT,
        "Muneoka Background Image",
        R.layout.view_image,
        Detail.builder()
            .setImagePath("Touristar/iwamiginzan/muneokake/infoboard/images/IMG_20180609_115300.png")
            .setSecondaryImagePath("Touristar/iwamiginzan/muneokake/infoboard/images/IMG_20180826_112846.png")
            .setFade(Detail.KEY_FADE_RIGHT_WIDTH, 0.4f)
            .setAllowZoom(true)
            .addSendsEvent(Event.EVENT_HIDECONTENT, null)
            .addSendsEvent(Event.EVENT_ZOOM, EventDetail.builder().setTitle("Muneoka Slide Area"))
            .addSendsEvent(Event.EVENT_SETMAINCONTENT, EventDetail.builder().setTitle("Muneoka Slide Area")),
        Vector3.zero(),
        new Vector3(0.415f, 0.572f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(-0.236f, 0.01f, 0.0f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_TEXTONIMAGE,
        Area.KIND_CONTENT,
        "Muneoka Background Intro",
        R.layout.view_text,
        Detail.builder()
            .setTextPath("Touristar/iwamiginzan/muneokake/infoboard/texts/infoboardtxt.html")
            .setHtmlPath("Touristar/iwamiginzan/muneokake/infoboard/texts/infoboard.html")
            .setBackgroundColor(Color.argb(0, 0, 0, 0))
            .setTextSize(12f),
        Vector3.zero(),
        new Vector3(0.445f, 0.572f, 0.2f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.106f, 0.01f, 0.32f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_ROTATIONBUTTON,
        Area.KIND_UI,
        "Muneoka Language Selector",
        // TODO: Remove and hardcode in Node
        R.layout.view_image,
        Detail.builder()
            .setImageResource(R.drawable.ic_language_selector)
            // TODO: Display language options with this button, and set the language with these buttons instead
            .addSendsEvent(Event.EVENT_SWITCHLANGUAGE, EventDetail.builder()
                .setLanguage(Detail.LANGUAGE_EN))
            .isCastingShadow(true),
        new Vector3(-.04f, 0.05f, 0.0f),
        new Vector3(0.08f, 0.1f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.475f, 0.01f, 0.155f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_ROTATIONBUTTON,
        Area.KIND_UI,
        "Main Content Grabber",
        R.layout.view_image,
        Detail.builder()
            .setImageResource(R.drawable.if_tiny_arrows_diagonal_out_1_252127)
            .addSendsEvent(Event.EVENT_GRABCONTENT, null)
            .isCastingShadow(true),
        new Vector3(-.04f, 0.05f, 0.0f),
        new Vector3(0.08f, 0.1f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.475f, 0.01f, 0.275f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_ROTATIONBUTTON,
        Area.KIND_UI,
        "Background Scaler",
        R.layout.view_image,
        Detail.builder()
            .setImageResource(R.drawable.ic_linear_scale_black_24dp)
            .addSendsEvent(Event.EVENT_SCALE, EventDetail.builder()
                .setScaleValues(Arrays.asList(0.5f, 1f, 3f)))
            .isCastingShadow(true),
        new Vector3(-.04f, 0.05f, 0.0f),
        new Vector3(0.08f, 0.1f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.475f, 0.01f, 0.035f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_SLIDESONIMAGE, Area.KIND_CONTENT,
        "Muneoka Slide Area",
        R.layout.view_slider,
        Detail.builder()
            .addSlide(new Slide(Slide.Companion.getTYPE_IMAGE(),
                "Touristar/iwamiginzan/muneokake/infoboard/images/igk_000_0682.jpg",
                null,
                "Image Slide 1"))
            .addSlide(new Slide(Slide.Companion.getTYPE_IMAGE(),
                "Touristar/iwamiginzan/muneokake/infoboard/images/igk_000_0693.jpg",
                null,
                "Image Slide 2"))
            .addSlide(new Slide(Slide.Companion.getTYPE_VR(),
                "Touristar/iwamiginzan/muneokake/infoboard/images/PANO_20180826_115346.jpg",
                null,
                "Panorama Slide"))
            .addSlide(new Slide(Slide.Companion.getTYPE_COMPARISON(),
                "Touristar/iwamiginzan/muneokake/infoboard/images/compare_new_color.png",
                Arrays.asList("Touristar/iwamiginzan/muneokake/infoboard/images/compare_old_color.png"),
                "Old New Comparision")),
        Vector3.zero(),
        new Vector3(0.895f, 0.582f, 0.005f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.0f, 0.01f, 0.29f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));
*/

//    viewModel.addArea(new Area(Area.TYPE_3DOBJECTONPLANE,
//        "Magistrate Office Building",
//        R.raw.office_full,
//        Vector3.zero(),
//        Area.COORDINATE_GLOBAL,
//        new Vector3(-0.2f, -0.7f, -1.75f),
//        new Quaternion(new Vector3(0.0f, -0.01f, 0.0f), 1.0f),
//        new Vector3(0.5f, 0.3f, 0.545f)));
//
//    viewModel.addArea(new Area(Area.TYPE_INTERACTIVEOVERLAY,
//        "Magistrate Office Building Schema",
//        R.drawable.office_schema_front,
//        new Vector3(0.415f, .0001f, 0.54f),
//        Area.COORDINATE_LOCAL,
//        new Vector3(0.2025f, 0.1f, 0.285f),
//        new Quaternion(-1f, 0f, 0f, 1.0f),
//        Vector3.one()));
//
//    viewModel.addArea(new Area(Area.TYPE_SLIDESONIMAGE,
//        "Magistrate Office Building Explanation",
//        R.drawable.magistrates_office_jp,
//        new Vector3(0.42f, 0.0001f, 0.24f),
//        Area.COORDINATE_LOCAL,
//        new Vector3(-0.22f, 0.01f, -0.10f),
//        new Quaternion(0f, 0f, 0f, 1f),
//        Vector3.one()));
//
//    viewModel.addArea(new Area(Area.TYPE_SLIDESONIMAGE,
//        "Magistrate Office Building Map",
//        R.drawable.goryoukaku_map,
//        new Vector3(0.247f, .001f, 0.247f),
//        Area.COORDINATE_LOCAL,
//        new Vector3(-0.2125f, 0.01f, 0.1565f),
//        new Quaternion(0f, 0f, 0f, 1f),
//        Vector3.one()));
//
//    viewModel.addArea(new Area(Area.TYPE_INTERACTIVEOVERLAY,
//        "Magistrate Office Building Schema",
//        R.drawable.office_schema_back,
//        new Vector3(0.415f, .0001f, 0.54f),
//        Area.COORDINATE_LOCAL,
//        new Vector3(0.2025f, 0.01f, 0.285f),
//        new Quaternion((float) -(Math.PI / 2), 0f, 0f, 1f),
//        Vector3.one()));
//
//    viewModel.addArea(new Area(Area.TYPE_INTERACTIVEPANEL,
//        "Interactive panel",
//        R.layout.user_panel,
//        new Vector3(0.24f, 0.001f, 0.60f),
//        Area.COORDINATE_LOCAL,
//        new Vector3(0.55f, 0.01f, 0.13f),
//        new Quaternion((float) -1, 0f, 0f, 1f),
//        Vector3.one()));
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