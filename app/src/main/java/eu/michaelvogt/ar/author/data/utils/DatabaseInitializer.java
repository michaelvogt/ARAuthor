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

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AreaDao;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.Location;
import eu.michaelvogt.ar.author.data.LocationDao;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.data.MarkerArea;
import eu.michaelvogt.ar.author.data.MarkerAreaDao;
import eu.michaelvogt.ar.author.data.MarkerDao;

public class DatabaseInitializer {
  private final LocationDao locationDao;
  private final MarkerDao markerDao;
  private final AreaDao areaDao;
  private final MarkerAreaDao markerAreaDao;

  private DatabaseInitializer(
      LocationDao locationDao, MarkerDao markerDao, AreaDao areaDao, MarkerAreaDao markerAreaDao) {
    this.locationDao = locationDao;
    this.markerDao = markerDao;
    this.areaDao = areaDao;
    this.markerAreaDao = markerAreaDao;
  }

  public static DatabaseInitializer runner(
      LocationDao locationDao, MarkerDao markerDao, AreaDao areaDao, MarkerAreaDao markerAreaDao) {
    return new DatabaseInitializer(
        locationDao, markerDao, areaDao, markerAreaDao);
  }

  public void run() {
    int locationId = insertLocation(new Location(
        "石見銀山",
        "Touristar/iwamiginzan/images/igk_machinami.jpg",
        "Touristar/iwamiginzan/intro.html"));

    insertMarker(new Marker("城上神社", locationId));
    int markerId = insertMarker(new Marker(
        "/Touristar/Markers/IMG_20180522_105701.jpg",
        "",
        "看板",
        "拝殿正面は一〇、ニニメートル、側面は二、八八メートル。屋根は重曹式の入母屋造り瓦葺きで江戸の亀戸天満宮を天本にしたものと伝えています。",
        "城上神社",
        0.6f,
        Vector3.zero(),
        new Vector3(1.4f, 0.005f, 0.715f),
        false,
        locationId,
        false));

    markerId = insertMarker(new Marker(
        "/Touristar/Markers/P_20180806_132544_vHDR_On.jpg",
        "",
        "鳴き龍",
        "",
        "城上神社",
        0f,
        Vector3.zero(),
        Vector3.zero(),
        false,
        locationId,
        false));

    markerId = insertMarker(new Marker(
        "/Touristar/Markers/IMG_20180522_105416.jpg",
        "",
        "亀石",
        "その昔延喜年間（一〇〇〇年ぐらい前）城上神社が仁摩町馬路の城上山に有った時、海防の神と崇められ崇敬者から珍しい貝の化石の有る石と海亀の",
        "城上神社",
        0.5f,
        Vector3.zero(),
        new Vector3(1.364f, 0.005f, 0.715f),
        false,
        locationId,
        false));

    insertMarker(new Marker("陣屋", locationId));
    markerId = insertMarker(new Marker(
        "/Touristar/Markers/IMG_20180423_132914.jpg",
        "",
        "中央看板",
        "陣屋は神社の東側にある本陣屋と銀山川をはさみその南側にある向陣屋によって構成されていた。また、向陣屋側には年貢銀や灰吹銀を保管するため",
        "陣屋",
        0.81f,
        Vector3.zero(),
        new Vector3(1.80f, 0.005f, 0.98f),
        false,
        locationId,
        false));

    markerId = insertMarker(new Marker(
        "/Touristar/Markers/IMG_20180423_132802.jpg",
        "",
        "左側看板",
        "",
        "陣屋",
        0.655f,
        Vector3.zero(),
        new Vector3(1.67f, 0.005f, 0.743f),
        false,
        locationId,
        false));

    markerId = insertMarker(new Marker(
        "/Touristar/Markers/P_20180806_135120_vHDR_On.jpg",
        "",
        "奥場",
        "",
        "陣屋",
        0f,
        Vector3.zero(),
        Vector3.zero(),
        false,
        locationId,
        false));


    insertMarker(new Marker("熊谷家", locationId));
    markerId = insertMarker(new Marker(
        "/Touristar/Markers/IMG_20180522_115942.jpg",
        "",
        "看板",
        "熊谷家は、文献によると十七世紀に石見銀山の経営に携わり、その後掛屋や郷宿、代官所の御用達を勤めたことが知られている。当主は代々町役人",
        "熊谷家",
        0.32f,
        Vector3.zero(),
        new Vector3(0.6f, 0.005f, 0.4f),
        false,
        locationId,
        false));


    insertMarker(new Marker("おかけ", locationId));
    markerId = insertMarker(new Marker(
        "/Touristar/Markers/P_20180801_112411_vHDR_On.jpg",
        "",
        "テンプ",
        "",
        "おかけ",
        0f,
        Vector3.zero(),
        Vector3.zero(),
        false,
        locationId,
        false));


    insertMarker(new Marker("川島家", locationId));
    markerId = insertMarker(new Marker(
        "/Touristar/Markers/P_20180804_182308_vHDR_On.jpg",
        "",
        "名板",
        "建物は、代官所の銀山方役所に勤務する銀山附役人河島氏の居宿であった。初代三郎右衛門は安芸国（広島県）出身で、慶長十五ねん（一六一〇）",
        "川島家",
        0f,
        Vector3.zero(),
        new Vector3(0.29f, 0.005f, 1.018f),
        false,
        locationId,
        false));


    insertMarker(new Marker("宗岡家", locationId));
    markerId = insertMarker(new Marker(
        "/Touristar/Markers/P_20180804_175049_vHDR_On.jpg",
        "/Touristar/iwamiginzan/muneokake/boardbackground.png",
        "看板",
        "宗岡は、戦国時代毛利輝元の「銀山六人衆」として知られ、諸役の徴収方を担いました。江戸時代に入ると、奉行大久保長安に召し抱えられ",
        "宗岡家",
        0.435f,
        Vector3.zero(),
        new Vector3(0.945f, 0.005f, 0.632f),
        true,
        locationId,
        false));

    int areaId = insertAreaForMarker(markerId, new Area(
        AreaVisual.TYPE_IMAGEONIMAGE,
        AreaVisual.KIND_CONTENT,
        "Muneoka Background Image"));

//    R.layout.view_image,
//        Vector3.zero(),
//        new Vector3(0.415f, 0.572f, 0.01f),
//        AreaVisual.COORDINATE_LOCAL,
//        new Vector3(-0.236f, 0.01f, 0.0f),
//        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
//        Vector3.one()


//    AreaVisual.builder(areaId)
//        .setImagePath("Touristar/iwamiginzan/muneokake/infoboard/images/IMG_20180609_115300.png")
//        .setSecondaryImagePath("Touristar/iwamiginzan/muneokake/infoboard/images/IMG_20180826_112846.png")
//        .setFade(AreaVisual.KEY_FADE_RIGHT_WIDTH, 0.4f)
//        .setAllowZoom(true);

//          .addSendsEvent(EventDetail.EVENT_HIDECONTENT, null)
//          .addSendsEvent(EventDetail.EVENT_ZOOM, EventDetail.builder().setZoomId(areaId))
//          .addSendsEvent(EventDetail.EVENT_SETMAINCONTENT, EventDetail.builder().setTitle("Muneoka Slide Area")),


    insertAreaForMarker(markerId, new Area(
        AreaVisual.TYPE_TEXTONIMAGE,
        AreaVisual.KIND_CONTENT,
        "Muneoka Background Intro"));

//    R.layout.view_text,
//        Vector3.zero(),
//        new Vector3(0.445f, 0.572f, 0.2f),
//        AreaVisual.COORDINATE_LOCAL,
//        new Vector3(0.106f, 0.01f, 0.32f),
//        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
//        Vector3.one()

//      AreaVisual.builder()
//          .setTextPath("Touristar/iwamiginzan/muneokake/infoboard/texts/infoboardtxt.html")
//          .setHtmlPath("Touristar/iwamiginzan/muneokake/infoboard/texts/infoboard.html")
//          .setBackgroundColor(Color.argb(0, 0, 0, 0))
//          .setTextSize(12f),


    insertAreaForMarker(markerId, new Area(
        AreaVisual.TYPE_ROTATIONBUTTON,
        AreaVisual.KIND_UI,
        "Muneoka Language Selector"));

    // TODO: Remove and hardcode in Node?
//    R.layout.view_image,
//        new Vector3(-.04f, 0.05f, 0.0f),
//        new Vector3(0.08f, 0.1f, 0.01f),
//        AreaVisual.COORDINATE_LOCAL,
//        new Vector3(0.475f, 0.01f, 0.155f),
//        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
//        Vector3.one()

//      AreaVisual.builder()
//          .setImageResource(R.drawable.ic_language_selector)
    // TODO: Display language options with this button, and set the language with these buttons instead
//              .addSendsEvent(Event.EVENT_SWITCHLANGUAGE, EventDetail.builder().setLanguage(AreaVisual.LANGUAGE_EN))
//          .isCastingShadow(true),


    insertAreaForMarker(markerId, new Area(
        AreaVisual.TYPE_ROTATIONBUTTON,
        AreaVisual.KIND_UI,
        "Main Content Grabber"));

//    R.layout.view_image,
//        new Vector3(-.04f, 0.05f, 0.0f),
//        new Vector3(0.08f, 0.1f, 0.01f),
//        AreaVisual.COORDINATE_LOCAL,
//        new Vector3(0.475f, 0.01f, 0.275f),
//        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
//        Vector3.one()

//      AreaVisual.builder()
//          .setImageResource(R.drawable.if_tiny_arrows_diagonal_out_1_252127)
//              .addSendsEvent(Event.EVENT_GRABCONTENT, null)
//          .isCastingShadow(true),


    insertAreaForMarker(markerId, new Area(
        AreaVisual.TYPE_ROTATIONBUTTON,
        AreaVisual.KIND_UI,
        "Background Scaler"));

//    R.layout.view_image,
//        new Vector3(-.04f, 0.05f, 0.0f),
//        new Vector3(0.08f, 0.1f, 0.01f),
//        AreaVisual.COORDINATE_LOCAL,
//        new Vector3(0.475f, 0.01f, 0.035f),
//        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
//        Vector3.one()

//      AreaVisual.builder()
//          .setImageResource(R.drawable.ic_linear_scale_black_24dp)
//              .addSendsEvent(Event.EVENT_SCALE, EventDetail.builder().setScaleValues(Arrays.asList(0.5f, 1f, 3f)))
//          .isCastingShadow(true),


    insertAreaForMarker(markerId, new Area(
        AreaVisual.TYPE_SLIDESONIMAGE,
        AreaVisual.KIND_CONTENT,
        "Muneoka Slide Area"));

//    R.layout.view_slider,
//        Vector3.zero(),
//        new Vector3(0.895f, 0.582f, 0.005f),
//        AreaVisual.COORDINATE_LOCAL,
//        new Vector3(0.0f, 0.01f, 0.29f),
//        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
//        Vector3.one()

//      AreaVisual.builder()
//          .addSlide(new Slide(Slide.Companion.getTYPE_IMAGE(),
//              "Touristar/iwamiginzan/muneokake/infoboard/images/igk_000_0682.jpg",
//              null,
//              "Image Slide 1"))
//          .addSlide(new Slide(Slide.Companion.getTYPE_IMAGE(),
//              "Touristar/iwamiginzan/muneokake/infoboard/images/igk_000_0693.jpg",
//              null,
//              "Image Slide 2"))
//          .addSlide(new Slide(Slide.Companion.getTYPE_VR(),
//              "Touristar/iwamiginzan/muneokake/infoboard/images/PANO_20180826_115346.jpg",
//              null,
//              "Panorama Slide"))
//          .addSlide(new Slide(Slide.Companion.getTYPE_COMPARISON(),
//              "Touristar/iwamiginzan/muneokake/infoboard/images/compare_new_color.png",
//              Arrays.asList("Touristar/iwamiginzan/muneokake/infoboard/images/compare_old_color.png"),
//              "Old New Comparision")),


    insertMarker(new Marker("金森家", locationId));
    insertMarker(new Marker(
        "/Touristar/Markers/P_20180804_175413_vHDR_On.jpg",
        "",
        "看板",
        "江戸時代、石見銀山附御料百五十余村は支配上六組に分けられていた。十八世紀の中頃には、大森には六軒の郷宿が設けられ、公用で出かける村役人",
        "金森家",
        0.36f,
        Vector3.zero(),
        new Vector3(0.6f, 0.005f, 0.4f),
        false,
        locationId,
        false));


    insertMarker(new Marker("五百羅漢", locationId));
    insertMarker(new Marker(
        "/Touristar/Markers/P_20180804_180510_vHDR_On.jpg",
        "",
        "入り口",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        Vector3.zero(),
        false,
        locationId,
        false));

    insertMarker(new Marker(
        "/Touristar/Markers/P_20180804_180605_vHDR_On.jpg",
        "",
        "１",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        Vector3.zero(),
        false,
        locationId,
        false));

    insertMarker(new Marker(
        "/Touristar/Markers/P_20180804_180654_vHDR_On.jpg",
        "",
        "２",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        Vector3.zero(),
        false,
        locationId,
        false));

    insertMarker(new Marker(
        "/Touristar/Markers/P_20180804_180746_vHDR_On.jpg",
        "",
        "３",
        "",
        "五百羅漢",
        0f,
        Vector3.zero(),
        Vector3.zero(),
        false,
        locationId,
        false));


    insertMarker(new Marker("豊坂神社", locationId));
    insertMarker(new Marker(
        "/Touristar/Markers/P_20180806_141327_vHDR_On.jpg",
        "",
        "看板",
        "毛利元就が祭神の、毛利家ゆかりの社である。元就は生前、自分の木像を造り山吹城に安置させたが、元就の孫の毛利輝元が洞春山長安寺を建立し",
        "豊坂神社",
        0.45f,
        Vector3.zero(),
        new Vector3(0.6f, 0.005f, 0.4f),
        false,
        locationId,
        false));


    insertMarker(new Marker("佐毘売神社", locationId));
    insertMarker(new Marker(
        "/Touristar/Markers/P_20180806_143800_vHDR_On.jpg",
        "",
        "看板",
        "この祭神は、鉱山の守り神である金山彦の神で、別なでは、「山神社」といい、鉱末や村人からは「山神さん」と呼ばれ親しまれていました。",
        "佐毘売神社",
        0.535f,
        Vector3.zero(),
        new Vector3(1.02f, 0.005f, 0.685f),
        false,
        locationId,
        false));


    insertMarker(new Marker("谷地区", locationId));
    insertMarker(new Marker(
        "/Touristar/Markers/P_20180806_144336_vHDR_On.jpg",
        "",
        "看板",
        "この説明版の左側、少し高くなった場所に鉱山の神である金山彦命を祀る佐毘売山神社があります。参道から佐毘売山神社に向って左側の谷出土谷、",
        "谷地区",
        1.04f,
        Vector3.zero(),
        new Vector3(1.35f, 0.005f, 0.965f),
        false,
        locationId,
        false));


    locationId = insertLocation(new Location(
        "箱館",
        "Touristar/hakodate/images/goryokakumainhall.jpg",
        "Touristar/hakodate/intro.html"));

    insertMarker(new Marker("箱館", locationId));
    markerId = insertMarker(new Marker(
        "/Touristar/Markers/office_front.jpg",
        "",
        "奉行菅",
        "",
        "五稜郭",
        0.9f,
        Vector3.zero(),
        new Vector3(),
        true,
        locationId,
        false));

    insertAreaForMarker(markerId, new Area(
        AreaVisual.TYPE_3DOBJECTONPLANE,
        AreaVisual.KIND_CONTENT,
        "Magistrate Office Building"));

//    R.raw.default_model,
//        Vector3.zero(),
//        new Vector3(-0.2f, -0.7f, -1.75f),
//        AreaVisual.COORDINATE_GLOBAL,
//        new Vector3(0.5f, 0.3f, 0.545f),
//        new Quaternion(new Vector3(0.0f, -0.01f, 0.0f), 1.0f),
//        Vector3.one()

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

  private int insertLocation(Location location) {
    return Math.toIntExact(locationDao.insert(location));
  }

  private int insertMarker(Marker marker) {
    return Math.toIntExact(markerDao.insert(marker));
  }

  private int insertAreaForMarker(int markerId, Area area) {
    int areaId = Math.toIntExact(areaDao.insert(area));
    insertMarkerArea(markerId, areaId);

    return areaId;
  }

  private void insertMarkerArea(int markerId, int areaId) {
    markerAreaDao.insert(new MarkerArea(markerId, areaId));
  }
}
