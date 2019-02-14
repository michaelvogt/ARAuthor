/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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

import android.content.Context
import android.util.Log
import com.google.ar.sceneform.math.Vector3
import eu.michaelvogt.ar.author.data.*
import org.json.JSONObject

class Json {
    companion object {
        private val TAG = Json::class.java.simpleName

        fun importLocation(context: Context?, viewModel: AuthorViewModel, location: Location, onFinished: () -> Unit) {
            val assetManager = context?.assets

            if (assetManager != null) {
                val moduleId = location.moduleId.also {
                    when (it) {
                        null, "" -> {
                            Log.e(TAG, "Module ID of location ${location.name} is missing. Required to access content")
                        }
                    }
                }

                val reader = assetManager.open("$moduleId.json").bufferedReader()
                val contentString = reader.readText()
                reader.close()

                if (location.isLoaded == false) {
                    // Insert content
                    val locationObject = JSONObject(contentString).getJSONObject("location")
                    val groupArray = locationObject.getJSONArray("groups")

                    for (groupIndex in 0 until groupArray.length()) {
                        val groupObject = groupArray.getJSONObject(groupIndex)

                        viewModel.insertTitleGroup(TitleGroup(groupObject.getString("name"))).thenAccept { groupId ->
                            val markersArray = groupObject.getJSONArray("markers")

                            for (markerIndex in 0 until markersArray.length()) {
                                val markerObject = markersArray.getJSONObject(markerIndex)

                                // TODO: Load complete Marker into DB
                                viewModel.insertMarker(Marker(location.uId, groupId,
                                        markerObject.getString("title"),
                                        zeroPoint = Converters().vector3FromString(markerObject.getString("zero_point"))
                                                ?: Vector3.zero())
                                ).thenAccept { markerId ->
                                    val areasArray = markerObject.getJSONArray("areas")

                                    for (areaIndex in 0 until areasArray.length()) {
                                        val areaObject = areasArray.getJSONObject(areaIndex)

                                        // TODO: Load complete Area into DB
                                        viewModel.insertArea(Area(areaObject.getString("title"))).thenAccept { areaId ->
                                            viewModel.insertMarkerArea(MarkerArea(markerId, areaId))
                                        }.exceptionally {
                                            Log.e(TAG, "Unable to insert area.", it)
                                            null
                                        }
                                    }
                                }.exceptionally {
                                    Log.e(TAG, "Unable to insert marker.", it)
                                    null
                                }
                            }
                        }.exceptionally {
                            Log.e(TAG, "Unable to insert group", it)
                            null
                        }
                    }

                    location.isLoaded = true
                    viewModel.updateLocation(location).thenAccept {
                        onFinished()
                    }
                } else {
                    // TODO: Update content
                }
            }
        }
    }
}
