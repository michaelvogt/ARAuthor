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

package eu.michaelvogt.ar.author

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.ar.core.ArCoreApk
import eu.michaelvogt.ar.author.data.AppDatabase
import eu.michaelvogt.ar.author.utils.*
import kotlinx.android.synthetic.main.fragment_permission_check.*
import java.util.concurrent.CompletableFuture

// TODO: Fetch from strings xml
private const val CAMERA_BUTTON = R.id.camera_req_btn
private const val CAMERA_APPROVED_TITLE = "Camera access approved"

private const val STORAGE_BUTTON = R.id.storage_req_btn
private const val STORAGE_APPROVED_TITLE = "Storage access approved"

private const val APPROVE_IN_SETTINGS_TITLE = "Approve in application settings"


class AuthorActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var deniedPermissions: ArrayList<String>

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val fab = findViewById<View>(R.id.fab)
        val bottomNav = findViewById<BottomAppBar>(R.id.bottom_nav)
        setSupportActionBar(bottomNav)

        navController.addOnNavigatedListener { _, destination ->
            // _ = controller
            when (destination.id) {
                R.id.intro_fragment,
                R.id.permission_check_fragment -> {
                    bottomNav.visibility = View.GONE
                    fab.visibility = View.GONE
                }
                else -> {
                    bottomNav.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                }
            }
        }

        val database = AppDatabase.getDatabase(applicationContext)

        // Fetching something seems to be the only way to open the database and trigger the callback
        CompletableFuture.supplyAsync { database!!.locationDao().getSize() }

        // Pre-Determine the availability of ARCore on the device, to have immediate access when needed
        ArCoreApk.getInstance().checkAvailability(this)
    }

    override
    fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val bottomSheetNav = BottomSheetNav()
                bottomSheetNav.show(supportFragmentManager, bottomSheetNav.tag)
                bottomSheetNav.setMenuSelectedListener(object : MenuSelectedListener {
                    override fun onMenuSelected(id: Int) {
                        when (id) {
                            R.id.location_list_fragment -> navController.navigate(R.id.location_list_fragment)
                            R.id.marker_list_fragment -> navController.navigate(R.id.marker_list_fragment)
                            R.id.area_list_fragment -> navController.navigate(R.id.area_list_fragment)
                        }
                    }
                })
                true
            }
            R.id.actionbar_feedback -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                        permissionApproved(CAMERA_PERMISSION, CAMERA_BUTTON, CAMERA_APPROVED_TITLE)
                    else ->
                        permissionDenied(CAMERA_PERMISSION, CAMERA_BUTTON, CAMERA_PERMISSION_CODE)
                }
            }

            STORAGE_PERMISSION_CODE -> {
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                        permissionApproved(STORAGE_PERMISSION, STORAGE_BUTTON, STORAGE_APPROVED_TITLE)
                    else ->
                        permissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_BUTTON, STORAGE_PERMISSION_CODE)
                }
            }
        }

        if (deniedPermissions.isEmpty()) {
            permissionsApproved()
        }
    }

    fun checkPermissions() {
        deniedPermissions = ArrayList()

        checkArcorePermission()

        when (checkSelfPermission(CAMERA_PERMISSION)) {
            PackageManager.PERMISSION_DENIED -> {
                permissionDenied(CAMERA_PERMISSION, CAMERA_BUTTON, CAMERA_PERMISSION_CODE)
            }
            PackageManager.PERMISSION_GRANTED -> {
                permissionApproved(CAMERA_PERMISSION, CAMERA_BUTTON, CAMERA_APPROVED_TITLE)
            }
        }

        when (checkSelfPermission(STORAGE_PERMISSION)) {
            PackageManager.PERMISSION_DENIED -> {
                permissionDenied(STORAGE_PERMISSION, STORAGE_BUTTON, STORAGE_PERMISSION_CODE)
            }
            PackageManager.PERMISSION_GRANTED -> {
                permissionApproved(STORAGE_PERMISSION, STORAGE_BUTTON, STORAGE_APPROVED_TITLE)
            }
        }

        if (deniedPermissions.isEmpty()) {
            permissionsApproved()
        }
    }

    fun checkArcorePermission() {
        if (useArcorePreference()) {
            when (ArCoreApk.getInstance().checkAvailability(this)) {
                ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> setupArcoreCheck(
                        R.string.arcore_cap_not_capable,
                        R.string.arcore_cap_btn_not_capable,
                        false, false)
                ArCoreApk.Availability.UNKNOWN_CHECKING,
                ArCoreApk.Availability.UNKNOWN_ERROR,
                ArCoreApk.Availability.UNKNOWN_TIMED_OUT -> Log.i("tag", "unknown")
                ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> Log.i("tag", "bi")
                ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD -> Log.i("tag", "old")
                ArCoreApk.Availability.SUPPORTED_INSTALLED -> setupArcoreCheck(
                        R.string.arcore_cap_ready,
                        R.string.arcore_cap_btn_ready,
                        true, false)
                else -> Log.i("AuthorActivity", "ARCore availability else")
            }
        } else {
            setupArcoreCheck(
                    R.string.arcore_cap_deactivated,
                    R.string.arcore_cap_btn_deactivated,
                    true, false)
        }
    }

    private fun useArcorePreference() = getPreference(this,
            resources.getString(R.string.checkbox_use_arcore_key),
            resources.getBoolean(R.bool.use_arcore_key_default))

    private fun setupArcoreCheck(statusResId: Int, titleResId: Int, checkBoxEnabled: Boolean, buttonEnabled: Boolean) {
        capable_arcore_status.text = resources.getString(statusResId)
        use_arcore_check.isEnabled = checkBoxEnabled
        capable_arcore_btn.text = resources.getString(titleResId)
        capable_arcore_btn.isEnabled = buttonEnabled
    }

    private fun permissionsApproved() {
        val approvedButton = findViewById<Button>(R.id.req_approved_button)
        req_approved_button.isEnabled = true
        approvedButton.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.action_locationlist)
        }
    }

    private fun permissionApproved(permission: String, res: Int, title: String) {
        deniedPermissions.remove(permission)

        val button = findViewById<Button>(res)
        button.isEnabled = false
        button.text = title
    }

    private fun permissionDenied(permission: String, res: Int, code: Int) {
        val button = findViewById<Button>(res)

        deniedPermissions.add(permission)

        if (shouldShowRequestPermissionRationale(permission)) {
            button.text = APPROVE_IN_SETTINGS_TITLE
            button.setOnClickListener { openAppSettings() }
        } else {
            button.setOnClickListener { requestPermissions(arrayOf(permission), code) }
        }
    }

    private fun openAppSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS

        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}