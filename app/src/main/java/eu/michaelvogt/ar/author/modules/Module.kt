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

package eu.michaelvogt.ar.author.modules

import android.content.Context

class Module {
    companion object {
        fun load(context: Context?, moduleId: String?, callback: () -> Unit) {
            // Can't be used currently, because of an incompatibility of play.core and sceneform
            // https://github.com/google-ar/sceneform-android-sdk/issues/507  --
            // TODO: Bug was just fixed, change for next release
            callback()

/*
            val splitManager = SplitInstallManagerFactory.create(context)
            val splitRequest = SplitInstallRequest.newBuilder().addModule(moduleId).build()

            splitManager.startInstall(splitRequest)
                    .addOnSuccessListener {
                        println("Module isLoaded. Session: $it")
                    }
                    .addOnFailureListener {
                        println("LocationListFragment error: $it")
                    }

            splitManager.registerListener { state ->
                state.moduleNames().forEach {
                    when (state.status()) {
                        SplitInstallSessionStatus.PENDING -> println("pending")
                        SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                            println("confirmation")
                            context.startIntentSender(
                                    state.resolutionIntent().getIntentSender(),
                                    null, 0, 0, 0)
                        }
                        SplitInstallSessionStatus.DOWNLOADING -> println("downloading")
                        SplitInstallSessionStatus.INSTALLING -> println("installing")
                        SplitInstallSessionStatus.INSTALLED -> println("installed")
                        SplitInstallSessionStatus.FAILED -> println("failed")
                        SplitInstallSessionStatus.CANCELING -> println("canceling")
                        SplitInstallSessionStatus.CANCELED -> println("canceled")
                    }
                }
            }
*/
        }
    }
}