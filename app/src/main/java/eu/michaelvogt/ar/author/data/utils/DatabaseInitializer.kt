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

package eu.michaelvogt.ar.author.data.utils

import android.graphics.Color
import android.util.Log
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.data.*
import java.util.*

class DatabaseInitializer private constructor(
        private val locationDao: LocationDao,
        private val markerDao: MarkerDao,
        private val areaDao: AreaDao,
        private val markerAreaDao: MarkerAreaDao,
        private val visualDetailDao: VisualDetailDao,
        private val slideDao: SlideDao,
        private val eventDetailDao: EventDetailDao) {

    fun run() {
        var locationId = insertLocation(
                "石見銀山",
                "Touristar/iwamiginzan/images/igk_machinami.jpg",
                "Touristar/iwamiginzan/intro.html")

        Log.i(DatabaseInitializer.TAG, "Location 石見銀山 $locationId inserted")

        insertMarkerForLocation(locationId, "城上神社")
        var markerId = insertMarkerForLocation(locationId,
                "看板",
                "/Touristar/Markers/IMG_20180522_105701.jpg",
                "",
                "拝殿正面は一〇、ニニメートル、側面は二、八八メートル。屋根は重曹式の入母屋造り瓦葺きで江戸の亀戸天満宮を天本にしたものと伝えています。",
                "城上神社",
                0.6f,
                Vector3.zero(),
                Vector3(1.4f, 0.005f, 0.715f),
                false,
                false)

        markerId = insertMarkerForLocation(locationId,
                "鳴き龍",
                "/Touristar/Markers/P_20180806_132544_vHDR_On.jpg",
                "",
                "",
                "城上神社",
                0f,
                Vector3.zero(),
                Vector3.zero(),
                false,
                false)

        markerId = insertMarkerForLocation(locationId,
                "亀石",
                "/Touristar/Markers/IMG_20180522_105416.jpg",
                "",
                "その昔延喜年間（一〇〇〇年ぐらい前）城上神社が仁摩町馬路の城上山に有った時、海防の神と崇められ崇敬者から珍しい貝の化石の有る石と海亀の",
                "城上神社",
                0.5f,
                Vector3.zero(),
                Vector3(1.364f, 0.005f, 0.715f),
                false,
                false)

        insertMarkerForLocation(locationId, "陣屋")
        markerId = insertMarkerForLocation(locationId,
                "中央看板",
                "/Touristar/Markers/IMG_20180423_132914.jpg",
                "",
                "陣屋は神社の東側にある本陣屋と銀山川をはさみその南側にある向陣屋によって構成されていた。また、向陣屋側には年貢銀や灰吹銀を保管するため",
                "陣屋",
                0.81f,
                Vector3.zero(),
                Vector3(1.80f, 0.005f, 0.98f),
                false,
                false)

        markerId = insertMarkerForLocation(locationId,
                "左側看板",
                "/Touristar/Markers/IMG_20180423_132802.jpg",
                "",
                "",
                "陣屋",
                0.655f,
                Vector3.zero(),
                Vector3(1.67f, 0.005f, 0.743f),
                false,
                false)

        markerId = insertMarkerForLocation(locationId,
                "奥場",
                "/Touristar/Markers/P_20180806_135120_vHDR_On.jpg",
                "",
                "",
                "陣屋",
                0f,
                Vector3.zero(),
                Vector3.zero(),
                false,
                false)


        insertMarkerForLocation(locationId, "熊谷家")
        markerId = insertMarkerForLocation(locationId,
                "看板",
                "/Touristar/Markers/IMG_20180522_115942.jpg",
                "",
                "熊谷家は、文献によると十七世紀に石見銀山の経営に携わり、その後掛屋や郷宿、代官所の御用達を勤めたことが知られている。当主は代々町役人",
                "熊谷家",
                0.32f,
                Vector3.zero(),
                Vector3(0.6f, 0.005f, 0.4f),
                false,
                false)


        insertMarkerForLocation(locationId, "おかけ")
        markerId = insertMarkerForLocation(locationId,
                "テンプ",
                "/Touristar/Markers/P_20180801_112411_vHDR_On.jpg",
                "",
                "",
                "おかけ",
                0f,
                Vector3.zero(),
                Vector3.zero(),
                false,
                false)


        insertMarkerForLocation(locationId, "川島家")
        markerId = insertMarkerForLocation(locationId,
                "名板",
                "/Touristar/Markers/P_20180804_182308_vHDR_On.jpg",
                "",
                "建物は、代官所の銀山方役所に勤務する銀山附役人河島氏の居宿であった。初代三郎右衛門は安芸国（広島県）出身で、慶長十五ねん（一六一〇）",
                "川島家",
                0f,
                Vector3.zero(),
                Vector3(0.29f, 0.005f, 1.018f),
                false,
                false)


        insertMarkerForLocation(locationId, "宗岡家")
        markerId = insertMarkerForLocation(locationId,
                "看板",
                "/Touristar/Markers/P_20180804_175049_vHDR_On.jpg",
                "/Touristar/iwamiginzan/muneokake/boardbackground.jpg",
                "宗岡は、戦国時代毛利輝元の「銀山六人衆」として知られ、諸役の徴収方を担いました。江戸時代に入ると、奉行大久保長安に召し抱えられ",
                "宗岡家",
                0.435f,
                Vector3.zero(),
                Vector3(0.945f, 0.005f, 0.632f),
                true,
                false)

        var areaId = insertAreaForMarker(markerId,
                "Muneoka Background Image",
                TYPE_IMAGEONIMAGE,
                KIND_CONTENT,
                GROUP_START,
                R.layout.view_image,
                Vector3.zero(),
                Vector3(0.415f, 0.572f, 0.001f),
                COORDINATE_LOCAL,
                Vector3(-0.436f, 0.001f, 0.0f),
                Quaternion(Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
                Vector3.one())

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_IMAGEPATH, "Touristar/iwamiginzan/muneokake/infoboard/images/IMG_20180609_115300.jpg")
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_SECONDARYIMAGEPATH, "Touristar/iwamiginzan/muneokake/infoboard/images/IMG_20180826_112846.jpg")
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_FADE_RIGHT_WIDTH, 0.4f)
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_ALLOWZOOM, true)

        insertEventDetail(areaId, EVENT_HIDECONTENT, NO_VALUE)
        insertEventDetail(areaId, EVENT_ZOOM, areaId)

        // TODO: replace string with uId of the area
        insertEventDetail(areaId, EVENT_SETMAINCONTENT, "Muneoka Slide Area")


        areaId = insertAreaForMarker(markerId,
                "Muneoka Background Intro",
                TYPE_TEXTONIMAGE,
                KIND_CONTENT,
                GROUP_START,
                R.layout.view_text,
                Vector3.zero(),
                Vector3(0.445f, 0.572f, 0.2f),
                COORDINATE_LOCAL,
                Vector3(0.106f, 0.01f, 0.32f),
                Quaternion(Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
                Vector3.one()
        )

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_TEXTPATH, "Touristar/iwamiginzan/muneokake/infoboard/texts/infoboardtxt.html")
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_HTMLPATH, "Touristar/iwamiginzan/muneokake/infoboard/texts/infoboard.html")
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_BACKGROUNDCOLOR, Color.argb(0, 0, 0, 0))
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_TEXTSIZE, 12f)

        areaId = insertAreaForMarker(markerId,
                "Muneoka Language Selector",
                TYPE_ROTATIONBUTTON,
                KIND_UI,
                GROUP_ALL,
                R.layout.view_image,    // TODO: Remove and hardcode in Node?
                Vector3(-.04f, 0.05f, 0.0f),
                Vector3(0.08f, 0.1f, 0.01f),
                COORDINATE_LOCAL,
                Vector3(0.475f, 0.01f, 0.155f),
                Quaternion(Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
                Vector3.one())

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_IMAGERESOURCE, R.drawable.ic_language_selector)
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_ISCASTINGSHADOW, true)

        // TODO: Display language options with this button, and set the language with these buttons instead
        insertEventDetail(areaId, EVENT_SWITCHLANGUAGE, LANGUAGE_EN)


        areaId = insertAreaForMarker(markerId,
                "Main Content Grabber",
                TYPE_ROTATIONBUTTON,
                KIND_UI,
                GROUP_ALL,
                R.layout.view_image,
                Vector3(-.04f, 0.05f, 0.0f),
                Vector3(0.08f, 0.1f, 0.01f),
                COORDINATE_LOCAL,
                Vector3(0.475f, 0.01f, 0.275f),
                Quaternion(Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
                Vector3.one())

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_IMAGERESOURCE, R.drawable.if_tiny_arrows_diagonal_out_1_252127)
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_ISCASTINGSHADOW, true)

        insertEventDetail(areaId, EVENT_GRABCONTENT, NO_VALUE)


        areaId = insertAreaForMarker(markerId,
                "Background Scaler",
                TYPE_ROTATIONBUTTON,
                KIND_UI,
                GROUP_ALL,
                R.layout.view_image,
                Vector3(-.04f, 0.05f, 0.0f),
                Vector3(0.08f, 0.1f, 0.01f),
                COORDINATE_LOCAL,
                Vector3(0.475f, 0.01f, 0.035f),
                Quaternion(Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
                Vector3.one())

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_IMAGERESOURCE, R.drawable.ic_linear_scale_black_24dp)
        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_ISCASTINGSHADOW, true)

        insertEventDetail(areaId, EVENT_SCALE, Arrays.asList(0.5f, 1f, 3f))


        areaId = insertAreaForMarker(markerId,
                "Muneoka Slide Area",
                TYPE_SLIDESONIMAGE,
                KIND_CONTENT,
                GROUP_NONE,
                R.layout.view_slider,
                Vector3.zero(),
                Vector3(0.895f, 0.582f, 0.005f),
                COORDINATE_LOCAL,
                Vector3(0.0f, 0.01f, 0.29f),
                Quaternion(Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
                Vector3.one())

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_SLIDES,
                0,
                TYPE_IMAGE,
                "Touristar/iwamiginzan/muneokake/infoboard/images/igk_000_0682.jpg",
                null,
                "Image Slide 1")

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_SLIDES,
                1,
                TYPE_IMAGE,
                "Touristar/iwamiginzan/muneokake/infoboard/images/igk_000_0693.jpg",
                null,
                "Image Slide 2"
        )

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_SLIDES,
                2,
                TYPE_IMAGE,
                "Touristar/iwamiginzan/muneokake/infoboard/images/PANO_20180826_115346.jpg",
                null,
                "Panorama Slide"
        )

        insertVisualDetail(areaId, TYPE_DETAIL_ALL,
                KEY_SLIDES,
                3,
                TYPE_IMAGE,
                "Touristar/iwamiginzan/muneokake/infoboard/images/compare_new_color.png",
                Arrays.asList("Touristar/iwamiginzan/muneokake/infoboard/images/compare_old_color.png"),
                "Old New Comparision"
        )


        insertMarkerForLocation(locationId, "金森家")
        insertMarkerForLocation(locationId,
                "看板",
                "/Touristar/Markers/P_20180804_175413_vHDR_On.jpg",
                "",
                "江戸時代、石見銀山附御料百五十余村は支配上六組に分けられていた。十八世紀の中頃には、大森には六軒の郷宿が設けられ、公用で出かける村役人",
                "金森家",
                0.36f,
                Vector3.zero(),
                Vector3(0.6f, 0.005f, 0.4f),
                false,
                false)


        insertMarkerForLocation(locationId, "五百羅漢")
        insertMarkerForLocation(locationId,
                "入り口",
                "/Touristar/Markers/P_20180804_180510_vHDR_On.jpg",
                "",
                "",
                "五百羅漢",
                0f,
                Vector3.zero(),
                Vector3.zero(),
                false,
                false)

        insertMarkerForLocation(locationId,
                "１",
                "/Touristar/Markers/P_20180804_180605_vHDR_On.jpg",
                "",
                "",
                "五百羅漢",
                0f,
                Vector3.zero(),
                Vector3.zero(),
                false,
                false)

        insertMarkerForLocation(locationId,
                "２",
                "/Touristar/Markers/P_20180804_180654_vHDR_On.jpg",
                "",
                "",
                "五百羅漢",
                0f,
                Vector3.zero(),
                Vector3.zero(),
                false,
                false)

        insertMarkerForLocation(locationId,
                "３",
                "/Touristar/Markers/P_20180804_180746_vHDR_On.jpg",
                "",
                "",
                "五百羅漢",
                0f,
                Vector3.zero(),
                Vector3.zero(),
                false,
                false)


        insertMarkerForLocation(locationId, "豊坂神社")
        insertMarkerForLocation(locationId,
                "看板",
                "/Touristar/Markers/P_20180806_141327_vHDR_On.jpg",
                "",
                "毛利元就が祭神の、毛利家ゆかりの社である。元就は生前、自分の木像を造り山吹城に安置させたが、元就の孫の毛利輝元が洞春山長安寺を建立し",
                "豊坂神社",
                0.45f,
                Vector3.zero(),
                Vector3(0.6f, 0.005f, 0.4f),
                false,
                false)


        insertMarkerForLocation(locationId, "佐毘売神社")
        insertMarkerForLocation(locationId,
                "看板",
                "/Touristar/Markers/P_20180806_143800_vHDR_On.jpg",
                "",
                "この祭神は、鉱山の守り神である金山彦の神で、別なでは、「山神社」といい、鉱末や村人からは「山神さん」と呼ばれ親しまれていました。",
                "佐毘売神社",
                0.535f,
                Vector3.zero(),
                Vector3(1.02f, 0.005f, 0.685f),
                false,
                false)


        insertMarkerForLocation(locationId, "谷地区")
        insertMarkerForLocation(locationId,
                "看板",
                "/Touristar/Markers/P_20180806_144336_vHDR_On.jpg",
                "",

                "この説明版の左側、少し高くなった場所に鉱山の神である金山彦命を祀る佐毘売山神社があります。参道から佐毘売山神社に向って左側の谷出土谷、",
                "谷地区",
                1.04f,
                Vector3.zero(),
                Vector3(1.35f, 0.005f, 0.965f),
                false,
                false)


        locationId = insertLocation(
                "箱館",
                "Touristar/hakodate/images/goryokakumainhall.jpg",
                "Touristar/hakodate/intro.html")

        Log.i(DatabaseInitializer.TAG, "Location 箱館 $locationId inserted")

        insertMarkerForLocation(locationId, "箱館")
        markerId = insertMarkerForLocation(locationId,
                "奉行菅",
                "/Touristar/Markers/office_front.jpg",
                "",
                "",
                "五稜郭",
                0.9f,
                Vector3.zero(),
                Vector3(),
                true,
                false)

        insertAreaForMarker(markerId,
                "Magistrate Office Building",
                TYPE_3DOBJECTONPLANE,
                KIND_CONTENT,
                GROUP_START,
                R.raw.default_model,
                Vector3.zero(),
                Vector3(-0.2f, -0.7f, -1.75f),
                COORDINATE_GLOBAL,
                Vector3(0.5f, 0.3f, 0.545f),
                Quaternion(Vector3(0.0f, -0.01f, 0.0f), 1.0f),
                Vector3.one())

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

        locationId = insertLocation(
                "中島",
                "Touristar/hiroshima/images/P_20180724_101817_vHDR_On.jpg",
                "Touristar/hiroshima/intro.html")
    }

    private fun insertLocation(name: String, thumbPath: String, introHtmlPath: String): Long {
        return locationDao.insert(Location(name, thumbPath, introHtmlPath))
    }

    private fun insertMarkerForLocation(locationId: Long, title: String): Long {
        return markerDao.insert(Marker(locationId, title, isTitle = true))
    }

    private fun insertMarkerForLocation(locationId: Long, title: String, markerImagePath: String,
                                        backgroundImagePath: String, intro: String, location: String,
                                        widthInM: Float, zeroPoint: Vector3, size: Vector3,
                                        showBackground: Boolean, isTitle: Boolean): Long {
        return markerDao.insert(Marker(locationId, title, markerImagePath,
                backgroundImagePath, intro, location, widthInM, zeroPoint, size, showBackground, isTitle))
    }

    private fun insertAreaForMarker(
            markerId: Long, backgroundImage: String, kind: Int, type: Int, group: Int, resource: Int,
            zeroPoint: Vector3, size: Vector3, coordType: Int, position: Vector3,
            rotation: Quaternion, scale: Vector3): Long {
        val areaId = areaDao.insert(Area(backgroundImage, kind, type, group, resource, zeroPoint,
                size, coordType, position, rotation, scale))
        insertMarkerArea(markerId, areaId)

        return areaId
    }

    private fun insertMarkerArea(markerId: Long, areaId: Long) {
        markerAreaDao.insert(MarkerArea(markerId, areaId))
    }

    private fun insertVisualDetail(areaId: Long, type: Int, key: Int, value: Any) : Long {
        return visualDetailDao.insert(VisualDetail(areaId, type, key, value))
    }

    private fun insertVisualDetail(areaId: Long, detailType: Int, key: Int, order: Int, valueType: Int,
                                   value: String, secondaryValues: List<String>?, description: String) {
        val detailId = insertVisualDetail(areaId, detailType, key, "")
        slideDao.insert(Slide(detailId, valueType, order, value, secondaryValues, description))
    }

    private fun insertEventDetail(areaId: Long, eventType: Int, eventValue: Any): Long {
        return eventDetailDao.insert(EventDetail(areaId, eventType, TYPE_EVENT_ALL, eventValue))
    }

    companion object {
        const val TAG: String = "DatabaseInitializer"

        fun runner(
                locationDao: LocationDao, markerDao: MarkerDao, areaDao: AreaDao,
                markerAreaDao: MarkerAreaDao, visualDetailDao: VisualDetailDao,
                slideDao: SlideDao, eventDetailDao: EventDetailDao): DatabaseInitializer {

            Log.i(TAG, "Start database initialisation")

            return DatabaseInitializer(
                    locationDao, markerDao, areaDao, markerAreaDao, visualDetailDao, slideDao, eventDetailDao)
        }
    }
}
