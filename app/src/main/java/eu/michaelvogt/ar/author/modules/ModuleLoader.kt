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
                    SplitInstallSessionStatus.CANCELING -> callback?.onInfo("canceling")
                    SplitInstallSessionStatus.CANCELED -> {
                        unregisterListener()
                        callback?.onCanceled()
                    }
                    SplitInstallSessionStatus.DOWNLOADING -> {
                        callback?.onProgress(state.bytesDownloaded(), state.totalBytesToDownload())
                    }
                    SplitInstallSessionStatus.DOWNLOADED -> callback?.onInfo("downloaded")
                    SplitInstallSessionStatus.FAILED -> {
                        unregisterListener()
                        callback?.onFailed(null)
                    }
                    SplitInstallSessionStatus.INSTALLING -> callback?.onInfo("installing")
                    SplitInstallSessionStatus.INSTALLED -> {
                        unregisterListener()
                        callback?.onSuccess()
                    }
                    SplitInstallSessionStatus.PENDING -> callback?.onInfo("pending")
                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
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
                    sessionId = it
                }
                .addOnFailureListener {
                    when ((it as SplitInstallException).errorCode) {
                        SplitInstallErrorCode.ACCESS_DENIED -> callback?.onFailed("Access denied")
                        SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> callback?.onFailed("Active session limit exceeded")
                        SplitInstallErrorCode.API_NOT_AVAILABLE -> callback?.onFailed("Api not available")
                        SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION -> callback?.onFailed("Incompatible with existing session")
                        SplitInstallErrorCode.INVALID_REQUEST -> callback?.onFailed("Invalid request")
                        SplitInstallErrorCode.NETWORK_ERROR -> callback?.onFailed("Network error: Connect to network")
                        SplitInstallErrorCode.SESSION_NOT_FOUND -> callback?.onFailed("Session not found")
                        SplitInstallErrorCode.SERVICE_DIED -> callback?.onFailed("Service died, please retry.")
                    }
                }
    }

    private fun unregisterListener() {
        splitManager.unregisterListener(updatedListener)
    }
}