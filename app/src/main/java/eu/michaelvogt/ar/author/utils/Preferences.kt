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

package eu.michaelvogt.ar.author.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.preference.PreferenceManager

class Preferences {
    companion object {
        fun setPreference(context: Context, @StringRes key: Int, value: Boolean) {
            val keyString = getKeyString(context, key)
            defaultPreferences(context)!!.edit().putBoolean(keyString, value).apply()
        }

        fun getPreference(context: Context?, @StringRes key: Int, default: Boolean): Boolean {
            val keyString = getKeyString(context, key)
            return defaultPreferences(context)?.getBoolean(keyString, true) ?: false
        }

        fun getPreference(context: Context, @StringRes key: Int, default: String): String {
            val keyString = getKeyString(context, key)
            return defaultPreferences(context)?.getString(keyString, default) ?: "Default"
        }

        private fun getKeyString(context: Context?, key: Int): String {
            return context?.resources?.getString(key) ?: ""
        }

        private fun defaultPreferences(context: Context?): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }
}