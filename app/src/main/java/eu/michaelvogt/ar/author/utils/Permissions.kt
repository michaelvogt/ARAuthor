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

package eu.michaelvogt.ar.author.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.ar.core.ArCoreApk
import eu.michaelvogt.ar.author.R
import kotlinx.android.synthetic.main.activity_author.*
import kotlinx.android.synthetic.main.fragment_permission_check.*

private lateinit var deniedPermissions: ArrayList<String>

/**
 * Checks all permissions of the application.
 * <p>
 * @param context       Activity context
 * @return              true when all required permissions are met, false otherwise
 */
fun meetsPermissionRequirements(context: Context?): Boolean {
    val arcoreAvailable = isArcoreAvailable(context)
    if (arcoreAvailable
            && ActivityCompat.checkSelfPermission(context!!,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        return true
    } else if (!arcoreAvailable
            && ActivityCompat.checkSelfPermission(context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        return true
    }

    return false
}

/**
 * Do a complete permission check and update the UI accordingly
 */
fun checkPermissions(activity: Activity, view: View) {
    var checkCameraPermission = true
    deniedPermissions = ArrayList()

    when (ArCoreApk.getInstance().checkAvailability(activity)) {
        ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> {
            checkCameraPermission = false
            setupArcoreCheck(activity,
                    R.string.arcore_cap_not_capable,
                    R.string.arcore_cap_btn_not_capable,
                    false)
        }
        ArCoreApk.Availability.UNKNOWN_CHECKING,
        ArCoreApk.Availability.UNKNOWN_ERROR,
        ArCoreApk.Availability.UNKNOWN_TIMED_OUT -> {
            checkCameraPermission = false
            setupArcoreCheck(activity,
                    R.string.arcore_cap_unknown,
                    R.string.arcore_cap_btn_unknown,
                    false)
        }
        ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> setupArcoreCheck(activity,
                R.string.arcore_cap_not_installed,
                R.string.arcore_cap_btn_not_installed,
                true)
        ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD -> setupArcoreCheck(activity,
                R.string.arcore_cap_too_old,
                R.string.arcore_cap_btn_too_old,
                true)
        ArCoreApk.Availability.SUPPORTED_INSTALLED -> setupArcoreCheck(activity,
                R.string.arcore_cap_ready,
                R.string.arcore_cap_btn_ready,
                false)
        else -> Log.i("AuthorActivity", "ARCore availability else")
    }

    val cameraBtn = view.findViewById<Button>(R.id.camera_req_btn)
    if (checkCameraPermission) {
        when (ActivityCompat.checkSelfPermission(activity, CAMERA_PERMISSION)) {
            PackageManager.PERMISSION_DENIED -> {
                permissionDenied(activity, CAMERA_PERMISSION, cameraBtn, CAMERA_PERMISSION_CODE)
            }
            PackageManager.PERMISSION_GRANTED -> {
                permissionApproved(CAMERA_PERMISSION,
                        cameraBtn, activity.getString(R.string.camera_req_btn_approved))
            }
        }
    } else {
        permissionUnnecessary(CAMERA_PERMISSION,
                cameraBtn, activity.getString(R.string.camera_req_btn_unnecessary))
    }

    val storageBtn = view.findViewById<Button>(R.id.storage_req_btn)
    when (ActivityCompat.checkSelfPermission(activity, STORAGE_PERMISSION)) {
        PackageManager.PERMISSION_DENIED -> {
            permissionDenied(activity, STORAGE_PERMISSION, storageBtn, STORAGE_PERMISSION_CODE)
        }
        PackageManager.PERMISSION_GRANTED -> {
            permissionApproved(STORAGE_PERMISSION,
                    storageBtn, activity.resources.getString(R.string.storage_req_btn_approved))
        }
    }

    if (deniedPermissions.isEmpty()) {
        permissionsApproved(activity)
    }
}

fun handleRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
    when (requestCode) {
        CAMERA_PERMISSION_CODE -> {
            when {
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    permissionApproved(CAMERA_PERMISSION, activity.camera_req_btn, activity.getString(R.string.camera_req_btn_approved))
                else -> {
                    permissionDenied(activity, CAMERA_PERMISSION, activity.camera_req_btn, CAMERA_PERMISSION_CODE)
                }
            }
        }

        STORAGE_PERMISSION_CODE -> {
            when {
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    permissionApproved(STORAGE_PERMISSION, activity.storage_req_btn, activity.getString(R.string.storage_req_btn_approved))
                else -> {
                    permissionDenied(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, activity.storage_req_btn, STORAGE_PERMISSION_CODE)
                }
            }
        }

        else ->
            throw IllegalArgumentException("Unhandled permision request result")
    }

    checkPermissions(activity, activity.app_layout)
}

fun permissionsApproved(activity: Activity) {
    val approvedBtn = activity.findViewById<Button>(R.id.req_approved_button)
    approvedBtn.isEnabled = true
}

fun permissionApproved(permission: String, button: Button, title: String) {
    deniedPermissions.remove(permission)

    button.isEnabled = false
    button.text = title
}

fun permissionDenied(activity: Activity, permission: String, button: Button, code: Int) {
    deniedPermissions.add(permission)

    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        button.text = activity.getString(R.string.APPROVE_IN_SETTINGS_TITLE)
        button.setOnClickListener { openAppSettings(activity) }
    } else {
        button.setOnClickListener { ActivityCompat.requestPermissions(activity, arrayOf(permission), code) }
    }
}

fun permissionUnnecessary(permission: String, button: Button, title: String) {
    deniedPermissions.remove(permission)

    button.text = title
    button.isEnabled = false
}

private fun setupArcoreCheck(activity: Activity, statusResId: Int, titleResId: Int, isButtonEnabled: Boolean) {
    val statusField = activity.findViewById<TextView>(R.id.capable_arcore_status)
    statusField.text = activity.getString(statusResId)

    val arcoreBtn = activity.findViewById<Button>(R.id.capable_arcore_btn)
    arcoreBtn.text = activity.getString(titleResId)
    arcoreBtn.isEnabled = isButtonEnabled
}

private fun openAppSettings(activity: Activity) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS

    val uri = Uri.fromParts("package", activity.packageName, null)
    intent.data = uri
    activity.startActivity(intent)
}

/**
 * Since ARCore checks its availability over the network, it needs to be polled until a
 * specific result is delivered.
 * <p>
 * Polling? There should be a better way
 * <p>
 * @param context   Activity context
 * @return          true when ARCore is available, false otherwise
 */
internal fun isArcoreAvailable(context: Context?): Boolean {
    val availability = ArCoreApk.getInstance().checkAvailability(context)
    if (availability.isTransient) {
        Handler().postDelayed({ isArcoreAvailable(context) }, 200)
    }

    if (availability.isSupported) {
        return true
    }

    return false
}