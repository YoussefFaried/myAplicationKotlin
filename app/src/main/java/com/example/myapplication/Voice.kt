package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.rtp.AudioCodec
import android.net.rtp.AudioGroup
import android.net.rtp.AudioStream
import android.net.rtp.RtpStream
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.text.format.Formatter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import java.net.InetAddress
import kotlin.experimental.and


class Voice : AppCompatActivity() {

    private lateinit var AuthFireBase: FirebaseAuth

    // init requierd objects
    var m_AudioGroup: AudioGroup? = null
    var m_AudioStream: AudioStream? = null
    var srcIP: TextView? = null
    var srcPort: TextView? = null
    var localIp = StringBuilder()
    var connect: ImageButton? = null
    var disconnect: ImageButton? = null
    var permissionGranted = false
    val REQUEST_CODE = 100
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        showDialog()
        AuthFireBase= FirebaseAuth.getInstance()


        // Check if the permissions are granted or not
        permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE
            )
        }

        if (!permissionGranted) return
        val srcIP = findViewById<TextView>(R.id.dynamicIpText)
        val srcPort = findViewById<TextView>(R.id.dynamicPortText)
        val connect = findViewById<ImageButton>(R.id.connectBtn)
        val disconnect = findViewById<Button>(R.id.disconnectBtn)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val audio = getSystemService(AUDIO_SERVICE) as AudioManager
            audio.mode = AudioManager.MODE_IN_COMMUNICATION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                m_AudioGroup = AudioGroup()
            }
            m_AudioGroup!!.mode = AudioGroup.MODE_NORMAL
            m_AudioStream = AudioStream(InetAddress.getByAddress(localIPAddress))
            val localPort = m_AudioStream!!.localPort
            Toast.makeText(this, localPort.toString() , Toast.LENGTH_LONG)
            srcPort.setText(localPort.toString() + "")
            srcIP.setText(localIp.deleteCharAt(localIp.length - 1).toString())
            m_AudioStream!!.codec = AudioCodec.PCMU
            m_AudioStream!!.mode = RtpStream.MODE_NORMAL
            disconnect.setEnabled(false)
            connect.setOnClickListener(View.OnClickListener {
                val remoteAddress =
                    (findViewById<View>(R.id.destIpText) as EditText).text.toString()
                val remotePort = (findViewById<View>(R.id.destPortText) as EditText).text.toString()
                try {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                            m_AudioStream!!.associate(
                                InetAddress.getByName(remoteAddress),
                                remotePort.toInt()
                            )
                        }
                        connect.setEnabled(false)
                        disconnect.setEnabled(true)
                        m_AudioStream!!.join(m_AudioGroup)
                    } catch (e: Exception) {
                        Toast.makeText(this@Voice, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // TODO Auto-generated catch block
                    Toast.makeText(this@Voice, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
            disconnect.setOnClickListener{
                Log.d("loggg","1")
                m_AudioGroup!!.clear()
                Log.d("loggg","2")

            }

        } catch (e: Exception) {
            Log.e("----------------------", e.toString())
            e.printStackTrace()
        }
    }

    // get the string ip
    private val localIPAddress:

    // convert to bytes
            ByteArray?
        private get() {
            var bytes: ByteArray? = null
            try {
                // get the string ip
                val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

                // convert to bytes
                var inetAddress: InetAddress? = null
                try {
                    inetAddress = InetAddress.getByName(ip)
                } catch (e: Exception) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                bytes = ByteArray(0)
                if (inetAddress != null) {
                    bytes = inetAddress.address
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            for (b: Byte in bytes!!) localIp.append((b.toUByte() and 0xFF.toUByte()).toString() + ".")

            return bytes
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    fun showDialog() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(this@Voice)
        alertDialogBuilder.setTitle("Help")
        //alertDialogBuilder.setIcon(R.drawable.help)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder.background =
                resources.getDrawable(R.drawable.rounded_edit_text, null)
        }*/
        alertDialogBuilder.setMessage(
            "1- Both sides should make sure that they gave the application the permission is asked for.\n" +
                    "2- Both sides should be in the same network. \n" +
                    "3- You should swap your local ip and ports appearing on the top of your screen.\n" +
                    "4- Both of you should click the green button to establish the connection.\n" +
                    "5- When any one needs to terminate they should simply click on the red button."
        )
        alertDialogBuilder.setNegativeButton(
            "Dismiss"
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        alertDialogBuilder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.itemExitApp -> {
                finish()
                val intentBack= Intent(this,CustomerActivity::class.java)
                startActivity(intentBack)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}