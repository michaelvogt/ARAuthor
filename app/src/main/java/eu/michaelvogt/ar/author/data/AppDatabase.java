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

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.utils.Converters;

@Database(entities = {Location.class, Marker.class, Area.class, MarkerArea.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
  public abstract LocationDao locationDao();

  public abstract MarkerDao markerDao();

  public abstract AreaDao areaDao();

  public abstract MarkerAreaDao markerAreaDao();

  private static AppDatabase INSTANCE;

  static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room
              .databaseBuilder(
                  context.getApplicationContext(), AppDatabase.class, "app_database")
              .fallbackToDestructiveMigration()
              .addCallback(sRoomDatabaseCallback)
              .build();
        }
      }
    }
    return INSTANCE;
  }

  private static AppDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
      super.onOpen(db);
      new PopulateDbAsync(INSTANCE).execute();
    }
  };

  private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final LocationDao locationDao;
    private final MarkerDao markerDao;
    private final AreaDao areaDao;

    private final MarkerAreaDao markerAreaDao;

    PopulateDbAsync(AppDatabase db) {
      locationDao = db.locationDao();
      markerDao = db.markerDao();
      areaDao = db.areaDao();

      markerAreaDao = db.markerAreaDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
      markerAreaDao.deleteAll();
      areaDao.deleteAll();
      markerDao.deleteAll();
      locationDao.deleteAll();

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

      insertMarker(new Marker(
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

      insertMarker(new Marker(
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
      insertMarker(new Marker(
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

      insertMarker(new Marker(
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

      insertMarker(new Marker(
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
      insertMarker(new Marker(
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
      insertMarker(new Marker(
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
      insertMarker(new Marker(
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

      int areaId = insertArea(new Area(Area.TYPE_IMAGEONIMAGE,
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

      insertMarkerArea(markerId, areaId);


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
      insertMarker(new Marker(
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

      return null;
    }

    private int insertLocation(Location location) {
      return Math.toIntExact(locationDao.insert(location));
    }

    private int insertMarker(Marker marker) {
      return Math.toIntExact(markerDao.insert(marker));
    }

    private int insertArea(Area area) {
      return Math.toIntExact(areaDao.insert(area));
    }

    private void insertMarkerArea(int markerId, int areaId) {
      Math.toIntExact(markerAreaDao.insert(new MarkerArea(markerId, areaId)));
    }
  }
}