package com.example.myapplicationtest

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_layout.*
import org.jetbrains.anko.toast
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class ControlActivity: AppCompatActivity(){

    companion object{
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var m_bluetoothSocket: BluetoothSocket? = null
        //private val mmSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m__bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
        lateinit var m_name: String
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
        //var mmInStream: InputStream?
        //var mmOutStream: OutputStream?
        private var mConnectedThread: ConnectedThread? = null
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
        ConnectToDevice(this).execute()

        test_button.setOnClickListener{ sendCommand("Hello World!")} //for now sending this
        control_led_disconnect.setOnClickListener{ disconnect()}


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



    fun sendCommand(input: String){
        if (m_bluetoothSocket != null){
            try{
                //Log.d("data", "DATA Incoming")
                Log.d("data", input)
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
        //receiveCommand()
    }

    private fun disconnect(){
        if (m_bluetoothSocket != null){
            try {
                m_isConnected = false
                m_bluetoothSocket!!.outputStream.close()
                m_bluetoothSocket!!.inputStream.close()
                m_bluetoothSocket!!.close()
            } catch (e: IOException){
                e.printStackTrace()
            } finally { // close the socket
                m_bluetoothSocket = null
                toast("Disconnecting from Server")
                //m_isConnected = false
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        finish()
    }



    private class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?

        var mHandler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                Log.i("data", "in handler")
                super.handleMessage(msg)
                when (msg.what) {
                    1 -> {
                        // DO something
                        val connectedThread = ConnectedThread((msg.obj as BluetoothSocket))
                        //Toast.makeText(getApplicationContext(), "CONNECT", Toast.LENGTH_SHORT).show()
                        val s = "successfully connected"
                        connectedThread.write(s.toByteArray())
                        Log.i("data", "connected")
                    }
                    2 -> {
                        val readBuf = msg.obj as ByteArray
                        val string = readBuf.toString(UTF_8)
                        Log.i("message", string)
                        //Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show()
                        write("11".toByteArray(UTF_8))
                        Log.i("message", "wrote 11")
                    }
                }
            }
        }

        override fun run() {
            val buffer = ByteArray(1024) // buffer store for the stream
            var bytes: Int // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            loop1@while (true) {
                try { // Read from the InputStream
                    try {
                        sleep(100)
                    } catch (e: InterruptedException) { // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    bytes = mmInStream!!.read(buffer)
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(2, bytes, -1, buffer).sendToTarget()
                } catch (e: IOException) {
                    loop1@break
                    Log.i("data","no longer running")
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        fun write(bytes: ByteArray?) {
            try {
                mmOutStream!!.write(bytes)
            } catch (e: IOException) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }
        }

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            // Get the input and output streams, using temp objects because
// member streams are final
            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
            }
            mmInStream = tmpIn
            mmOutStream = tmpOut
        }
    }






    private class ConnectToDevice(c: Context): AsyncTask<Void, Void, String>(){

        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }


        fun write(out: ByteArray?) { // Create temporary object
            var r: ConnectedThread
            // Synchronize a copy of the ConnectedThread
            synchronized(this) {
                if (m_isConnected != true) return
                r = mConnectedThread!!
            }
            // Perform the write unsynchronized
            r.write(out)
        }


        override fun onPreExecute(){
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "Please Wait")
        }

        override fun doInBackground(vararg p0: Void?): String?{
            try{
                if(m_bluetoothSocket == null || !m_isConnected){
                    m__bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m__bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    //stops the app for looking for other devices
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            }catch (e: IOException){
                connectSuccess =  false
                Log.d("data", "Failed to Connect \n")
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?){
            super.onPostExecute(result)
            if(!connectSuccess){
                Log.i("data", "Unable to Connect")
                val intent = Intent(this.context, MainActivity::class.java)
                this.context.toast("Failed to Connect to " + m_address)
                this.context.startActivity(intent)
            } else {
                Log.i("data", "Successfully Connected")
                m_isConnected = true
                mConnectedThread = ConnectedThread(m_bluetoothSocket!!)


            }
            m_progress.dismiss()
            if(m_isConnected == true) {
                write("11".toByteArray(UTF_8)) //This Works!!!
                mConnectedThread!!.run()
                //mConnectedThread!!.write("11".toByteArray())


            }

        }




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






// for disconnet(), if needed
/** finally {
    m_bluetoothSocket!!.close()
} */
