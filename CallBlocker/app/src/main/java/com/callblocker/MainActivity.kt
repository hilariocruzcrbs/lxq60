package com.callblocker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var toggleSwitch: Switch
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        toggleSwitch = findViewById(R.id.toggleSwitch)

        toggleSwitch.isChecked = BlockState.isEnabled(this)
        statusText.text = if (BlockState.isEnabled(this)) "Blocking: ON" else "Blocking: OFF"

        toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) requestNeededPerms() else {
                BlockState.setEnabled(this, false)
                statusText.text = "Blocking: OFF"
            }
        }
    }

    private fun requestNeededPerms() {
        val perms = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_OWN_CALLS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Settings.ACTION_MANAGE_OWN_CALLS_SETTINGS)
                startActivity(intent)
                toggleSwitch.isChecked = false
                Toast.makeText(this, "Grant MANAGE_OWN_CALLS in Settings, then toggle again", Toast.LENGTH_LONG).show()
                return
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 1001)
            return
        }

        enableBlocking()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableBlocking()
            } else {
                toggleSwitch.isChecked = false
                Toast.makeText(this, "Contacts permission required to check saved numbers", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun enableBlocking() {
        BlockState.setEnabled(this, true)
        toggleSwitch.isChecked = true
        statusText.text = "Blocking: ON"
        Toast.makeText(this, "Unknown callers will now be blocked", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        toggleSwitch.isChecked = BlockState.isEnabled(this)
        statusText.text = if (BlockState.isEnabled(this)) "Blocking: ON" else "Blocking: OFF"
    }
}
