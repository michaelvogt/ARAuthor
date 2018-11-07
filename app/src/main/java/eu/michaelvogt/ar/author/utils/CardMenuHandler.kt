package eu.michaelvogt.ar.author.utils

import android.view.View

import eu.michaelvogt.ar.author.data.Location

interface CardMenuHandler {
    fun onMenuClick(view: View, location: Location)
}
