package com.example.myapplicationtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_layout.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class ControlActivity: AppCompatActivity(){

    companion object{
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

        /*
        //toast("existence is main for a programmer")
        Log.i("data", "start of loop \n")
        loop@ while(true){
            if(m_isConnected == true){
                Log.i("data", "sent message \n")
                break@loop
            }
        }
        sendCommand("11")
        Log.i("data", "sent message \n")
        while(m_isConnected == true){
            Log.i("data", "ready to receive \n")
            receiveCommand()
            handlemessage(mmBuffer.toString(Charsets.UTF_8))
        }
        */

    }

}

fun encrypt(password: String, input: String):String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in 0 until charArray.size){
        iv[i] = charArray[i].toByte()
    }
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

    val encryptedValue = cipher.doFinal(input.toByteArray())
    //return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    return encryptedValue.toString()
}

fun String.decrypt(password: String, input: String): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in 0 until charArray.size){
        iv[i] = charArray[i].toByte()
    }
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

    //val decryptedByteValue = cipher.doFinal(Base64.decode(this, Base64.DEFAULT))
    val decryptedByteValue = cipher.doFinal(input.toByteArray())
    //cipher.doFinal()
    return String(decryptedByteValue)
}