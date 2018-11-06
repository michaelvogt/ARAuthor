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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import eu.michaelvogt.ar.author.data.AppDatabase
import java.util.concurrent.CompletableFuture

private const val CAMERA_BUTTON = R.id.camera_req_btn
private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
private const val CAMERA_APPROVED_TITLE = "Camera access approved"

private const val STORAGE_BUTTON = R.id.storage_req_btn
private const val STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
private const val STORAGE_APPROVED_TITLE = "Storage access approved"

private const val APPROVE_IN_SETTINGS_TITLE = "Approve in application settings"

private const val REQUESTCAMERA = 1
private const val REQUESTSTORAGE = 2


class AuthorActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var deniedPermissions = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        val bottomNav = findViewById<View>(R.id.bottom_nav) as BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController)

        val database = AppDatabase.getDatabase(applicationContext)

        // Fetching something seems to be the only way to open the database and trigger the callback
        CompletableFuture.supplyAsync { database!!.locationDao().getSize() }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_feedback -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUESTCAMERA -> {
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                        permissionApproved(CAMERA_PERMISSION, CAMERA_BUTTON, CAMERA_APPROVED_TITLE)
                    else ->
                        permissionDenied(CAMERA_PERMISSION, CAMERA_BUTTON, REQUESTCAMERA)
                }
            }

            REQUESTSTORAGE -> {
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                        permissionApproved(STORAGE_PERMISSION, STORAGE_BUTTON, STORAGE_APPROVED_TITLE)
                    else ->
                        permissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_BUTTON, REQUESTSTORAGE)
                }
            }
        }

        if (deniedPermissions.isEmpty()) {
            permissionsApproved()
        }
    }

    fun checkPermissions() {
        val cameraPermission = ActivityCompat.checkSelfPermission(applicationContext, CAMERA_PERMISSION)
        when (cameraPermission) {
            PackageManager.PERMISSION_DENIED -> {
                permissionDenied(CAMERA_PERMISSION, CAMERA_BUTTON, REQUESTCAMERA)
            }
            PackageManager.PERMISSION_GRANTED -> {
                permissionApproved(CAMERA_PERMISSION, CAMERA_BUTTON, CAMERA_APPROVED_TITLE)
            }
        }

        val storagePermission = ActivityCompat.checkSelfPermission(applicationContext, STORAGE_PERMISSION)
        when (storagePermission) {
            PackageManager.PERMISSION_DENIED -> {
                permissionDenied(STORAGE_PERMISSION, STORAGE_BUTTON, REQUESTSTORAGE)
            }
            PackageManager.PERMISSION_GRANTED -> {
                permissionApproved(STORAGE_PERMISSION, STORAGE_BUTTON, STORAGE_APPROVED_TITLE)
            }
        }

        if (deniedPermissions.isEmpty()) {
            permissionsApproved()
        }
    }

    private fun permissionsApproved() {
        val approvedButton = findViewById<Button>(R.id.req_approved_button)
        approvedButton.isEnabled = true
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

        when {
            shouldShowRequestPermissionRationale(permission) -> {
                button.text = APPROVE_IN_SETTINGS_TITLE
                button.setOnClickListener { openAppSettings() }
            }
            else ->
                button.setOnClickListener { ActivityCompat.requestPermissions(this, arrayOf(permission), code) }
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