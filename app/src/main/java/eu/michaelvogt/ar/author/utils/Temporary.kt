package eu.michaelvogt.ar.author.utils

import android.app.AlertDialog
import android.content.Context

fun notDoneYet(context: Context) {
    AlertDialog.Builder(context)
            .setMessage("Sorry, not done, yet")
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
            }
            .create()
            .show()
}
