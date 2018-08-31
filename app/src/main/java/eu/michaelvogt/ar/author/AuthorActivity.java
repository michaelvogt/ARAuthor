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
import android.graphics.Color;
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
import eu.michaelvogt.ar.author.data.Event;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.utils.Detail;

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
    viewModel.addMarker(new Marker("城上神社"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180522_105701.jpg",
        "",
        "看板",
        "拝殿正面は一〇、ニニメートル、側面は二、八八メートル。屋根は重曹式の入母屋造り瓦葺きで江戸の亀戸天満宮を天本にしたものと伝えています。",
        "城上神社",
        0.6f,
        new Vector3(1.4f, 0.005f, 0.715f),
        false,
        new ArrayList<>()));

    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180806_132544_vHDR_On.jpg",
        "",
        "鳴き龍",
        "",
        "城上神社",
        0f,
        Vector3.zero(),
        false,
        new ArrayList<>()));

    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180522_105416.jpg",
        "",
        "亀石",
        "その昔延喜年間（一〇〇〇年ぐらい前）城上神社が仁摩町馬路の城上山に有った時、海防の神と崇められ崇敬者から珍しい貝の化石の有る石と海亀の",
        "城上神社",
        0.5f,
        new Vector3(1.364f, 0.005f, 0.715f),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("陣屋"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180423_132914.jpg",
        "",
        "中央看板",
        "陣屋は神社の東側にある本陣屋と銀山川をはさみその南側にある向陣屋によって構成されていた。また、向陣屋側には年貢銀や灰吹銀を保管するため",
        "陣屋",
        0.81f,
        new Vector3(1.80f, 0.005f, 0.98f),
        false,
        new ArrayList<>()));

    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180423_132802.jpg",
        "",
        "左側看板",
        "",
        "陣屋",
        0.655f,
        new Vector3(1.67f, 0.005f, 0.743f),
        false,
        new ArrayList<>()));

    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180806_135120_vHDR_On.jpg",
        "",
        "奥場",
        "",
        "陣屋",
        0f,
        Vector3.zero(),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("熊谷家"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/IMG_20180522_115942.jpg",
        "",
        "看板",
        "熊谷家は、文献によると十七世紀に石見銀山の経営に携わり、その後掛屋や郷宿、代官所の御用達を勤めたことが知られている。当主は代々町役人",
        "熊谷家",
        0.32f,
        new Vector3(0.6f, 0.005f, 0.4f),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("おかけ"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180801_112411_vHDR_On.jpg",
        "",
        "テンプ",
        "",
        "おかけ",
        0f,
        Vector3.zero(),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("川島家"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180804_182308_vHDR_On.jpg",
        "",
        "名板",
        "建物は、代官所の銀山方役所に勤務する銀山附役人河島氏の居宿であった。初代三郎右衛門は安芸国（広島県）出身で、慶長十五ねん（一六一〇）",
        "川島家",
        0f,
        new Vector3(0.29f, 0.005f, 1.018f),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("宗岡家"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180804_175049_vHDR_On.jpg",
        "/Touristar/iwamiginzan/muneokake/boardbackground.png",
        "看板",
        "",
        "宗岡家",
        0.435f,
        new Vector3(0.945f, 0.005f, 0.632f),
        true,
        Arrays.asList(0, 1, 2, 3)));


    viewModel.addMarker(new Marker("金森家"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180804_175413_vHDR_On.jpg",
        "",
        "看板",
        "江戸時代、石見銀山附御料百五十余村は支配上六組に分けられていた。十八世紀の中頃には、大森には六軒の郷宿が設けられ、公用で出かける村役人",
        "金森家",
        0.36f,
        new Vector3(0.6f, 0.005f, 0.4f),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("五百羅漢"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180804_180510_vHDR_On.jpg",
        "",
        "入り口",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        false,
        new ArrayList<>()));

    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180804_180605_vHDR_On.jpg",
        "",
        "１",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        false,
        new ArrayList<>()));

    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180804_180654_vHDR_On.jpg",
        "",
        "２",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        false,
        new ArrayList<>()));

    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180804_180746_vHDR_On.jpg",
        "",
        "３",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("豊坂神社"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180806_141327_vHDR_On.jpg",
        "",
        "看板",
        "毛利元就が祭神の、毛利家ゆかりの社である。元就は生前、自分の木像を造り山吹城に安置させたが、元就の孫の毛利輝元が洞春山長安寺を建立し",
        "豊坂神社",
        0.45f,
        new Vector3(0.6f, 0.005f, 0.4f),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("佐毘売神社"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180806_143800_vHDR_On.jpg",
        "",
        "看板",
        "この祭神は、鉱山の守り神である金山彦の神で、別なでは、「山神社」といい、鉱末や村人からは「山神さん」と呼ばれ親しまれていました。",
        "佐毘売神社",
        0.535f,
        new Vector3(1.02f, 0.005f, 0.685f),
        false,
        new ArrayList<>()));


    viewModel.addMarker(new Marker("谷地区"));
    viewModel.addMarker(new Marker(
        "/Touristar/Markers/P_20180806_144336_vHDR_On.jpg",
        "",
        "看板",
        "この説明版の左側、少し高くなった場所に鉱山の神である金山彦命を祀る佐毘売山神社があります。参道から佐毘売山神社に向って左側の谷出土谷、",
        "谷地区",
        1.04f,
        new Vector3(1.35f, 0.005f, 0.965f),
        false,
        new ArrayList<>()));

//    viewModel.addMarker(new Marker(
//        "/Touristar/Markers/IMG_20180419_100912.jpg",
//        "hidakaya_sign",
//        "日高屋",
//        0.644f,
//        true,
//        new ArrayList<>()));
//
//    viewModel.addMarker(new Marker(
//        "/Touristar/Markers/IMG_20180419_125435.jpg",
//        "opera_sign",
//        "大森座",
//        0.671f,
//        true,
//        new ArrayList<>()));
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
//
//    viewModel.addMarker(new Marker(
//            "/Touristar/Markers/office_front.jpg",
//            "office_sign_front",
//            "五稜郭",
//            0.9f,
//            true,
//            new ArrayList<>(Arrays.asList(0, 1, 2, 3))));
//
//    viewModel.addMarker(new Marker(
//            "/Touristar/Markers/office_back.jpg",
//            "office_sign_back",
//            "五稜郭",
//            0.9f,
//            true,
//            new ArrayList<>(Arrays.asList(0, 4, 2, 3))));


    viewModel.addArea(new Area(Area.TYPE_IMAGEONIMAGE,
        "Muneoka Background Image",
        R.layout.view_image,
        Detail.builder()
            .setImagePath("Touristar/iwamiginzan/muneokake/infoboard/images/IMG_20180609_115300.png")
            .setFade(Detail.KEY_FADE_RIGHT_WIDTH, 0.4f)
            .setAllowZoom(true),
        new Vector3(0.415f, 0.572f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(-0.236f, 0.01f, 0.0f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_TEXTONIMAGE,
        "Muneoka Background Intro",
        R.layout.view_text,
        Detail.builder()
            .setTextPath("Touristar/iwamiginzan/muneokake/infoboard/texts/infoboardtxt.html")
            .setBackgroundColor(Color.argb(0, 0, 0, 0))
            .setTextSize(12f),
        new Vector3(0.445f, 0.572f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.106f, 0.01f, 0.32f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_IMAGEONIMAGE,
        "Muneoka Language Selector",
        // TODO: Remove and hardcode in Node
        R.layout.view_image,
        Detail.builder()
            .setImageResource(R.drawable.ic_language_selector)
            // TODO: Display language options with this button, and set the language with these buttons instead
            .setHandlesEvent(Event.EVENT_SWITCHLANGUAGE, Detail.LANGUAGE_EN)
            .isCastingShadow(true),
        new Vector3(0.0445f, 0.0572f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.475f, 0.01f, 0.205f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

    viewModel.addArea(new Area(Area.TYPE_IMAGEONIMAGE,
        "Main Content Grabber",
        R.layout.view_image,
        Detail.builder()
            .setImageResource(R.drawable.if_tiny_arrows_diagonal_out_1_252127)
            .isCastingShadow(true),
        new Vector3(0.0445f, 0.0572f, 0.01f),
        Area.COORDINATE_LOCAL,
        new Vector3(0.475f, 0.01f, 0.275f),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one()));

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