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

@file:JvmName("Constants")

package eu.michaelvogt.ar.author.utils

import android.Manifest
import eu.michaelvogt.ar.author.BuildConfig

const val NEW_CURRENT_LOCATION = -1L
const val NEW_CURRENT_MARKER = -1L
const val NEW_CURRENT_AREA = -1L


// Permissions
const val CAMERA_PERMISSION_CODE = 0
const val CAMERA_PERMISSION = Manifest.permission.CAMERA

const val STORAGE_PERMISSION_CODE = 1
const val STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

const val PATH_SELECT_THUMB_CODE = 2
const val PATH_SELECT_LOCATION_INTRO_CODE = 3

/**
 * Constructs a string with the current version of the app and the model of the phone.
 */
val versionAndDeviceString: String by lazy {
    "Version: ${BuildConfig.VERSION_NAME}<${BuildConfig.VERSION_CODE}>\n" +
            "Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
}
