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

import android.app.Activity
import android.widget.Toast
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import eu.michaelvogt.ar.author.utils.MODULE_USER_REQUEST_CODE

class ModuleLoader(val activity: Activity?) {
    private val updatedListener: SplitInstallStateUpdatedListener
    private val splitManager: SplitInstallManager = SplitInstallManagerFactory.create(activity)

    private var callback: ModuleLoaderCallback? = null
    private var sessionId: Int = 0
    private var moduleIds: Array<out String?> = emptyArray()

    init {
        updatedListener = SplitInstallStateUpdatedListener { state ->
            if (state.sessionId() == sessionId) {
                when (state.status()) {
                    SplitInstallSessionStatus.CANCELING -> showMessage("canceling")
                    SplitInstallSessionStatus.CANCELED -> {
                        showMessage("canceled")
                        unregisterListener()
                        callback?.onCanceled()
                    }
                    SplitInstallSessionStatus.DOWNLOADING -> {
                        showMessage("downloading")
                        callback?.onProgress(state.bytesDownloaded(), state.totalBytesToDownload())
                    }
                    SplitInstallSessionStatus.DOWNLOADED -> showMessage("downloaded")
                    SplitInstallSessionStatus.FAILED -> {
                        showMessage("failed")
                        unregisterListener()
                        callback?.onFailed()
                    }
                    SplitInstallSessionStatus.INSTALLING -> showMessage("installing")
                    SplitInstallSessionStatus.INSTALLED -> {
                        showMessage("installed")
                        unregisterListener()
                        callback?.onSuccess()
                    }
                    SplitInstallSessionStatus.PENDING -> showMessage("pending")
                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                        showMessage("confirmation")
                        splitManager.startConfirmationDialogForResult(state, activity, MODULE_USER_REQUEST_CODE)
                    }
                }
            }
        }

        splitManager.registerListener(updatedListener)
    }

    fun setModules(vararg moduleIds: String?): ModuleLoader {
        this.moduleIds = moduleIds
        return this
    }

    fun setCallback(callback: ModuleLoaderCallback): ModuleLoader {
        this.callback = callback
        return this
    }

    fun install() {
        val splitBuilder = SplitInstallRequest.newBuilder()
        moduleIds.forEach { splitBuilder.addModule(it) }

        splitManager
                .startInstall(splitBuilder.build())
                .addOnSuccessListener {
                    showMessage("ModuleLoader is loading. Session: $it")
                    sessionId = it
                }
                .addOnFailureListener {
                    showMessage("LocationListFragment error: $it")

                    when ((it as SplitInstallException).errorCode) {
                        SplitInstallErrorCode.ACCESS_DENIED -> showMessage("Access denied")
                        SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> showMessage("Active session limit exceeded")
                        SplitInstallErrorCode.API_NOT_AVAILABLE -> showMessage("Api not available")
                        SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION -> showMessage("Incompatible with existing session")
                        SplitInstallErrorCode.INVALID_REQUEST -> showMessage("Invalid request")
                        SplitInstallErrorCode.NETWORK_ERROR -> showMessage("Connect to network")
                        SplitInstallErrorCode.SESSION_NOT_FOUND -> showMessage("Session not found")
                        SplitInstallErrorCode.SERVICE_DIED -> showMessage("Service died")
                    }
                }
    }

    private fun unregisterListener() {
        splitManager.unregisterListener(updatedListener)
    }

    private fun showMessage(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        toast.show()
    }
}