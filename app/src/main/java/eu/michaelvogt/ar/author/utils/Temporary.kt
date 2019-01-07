package eu.michaelvogt.ar.author.utils

import android.app.AlertDialog
import android.content.Context

fun notDoneYet(context: Context, name: String) {
    AlertDialog.Builder(context)
            .setMessage("$name - not done yet")
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
            }
            .create()
            .show()
}
