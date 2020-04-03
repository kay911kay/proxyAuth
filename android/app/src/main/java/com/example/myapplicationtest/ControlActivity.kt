package com.example.myapplicationtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_layout.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class ControlActivity: AppCompatActivity() {
    companion object {
        lateinit var m_address: String
        lateinit var m_name: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_layout)

        m_address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS)
        m_name = intent.getStringExtra(MainActivity.DEVICE_NAME)

        val cDevice = "Connected Device: $m_name"
        val cDeviceAddr = "Device Address: $m_address"
        connectedDevice.text = cDevice
        deviceAddress.text = cDeviceAddr

        // connecting to the device
        val intent = Intent(this, BluetoothInteraction::class.java)
        intent.putExtra(MainActivity.EXTRA_ADDRESS, m_address)
        intent.putExtra(MainActivity.DEVICE_NAME, m_name)
        intent.also { intent ->
            startService(intent)
        }
        disconnect.setOnClickListener { stopService(intent) }
    }
}