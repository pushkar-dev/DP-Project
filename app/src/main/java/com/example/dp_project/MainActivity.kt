package com.example.dp_project

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemAdapter=Adapters(this.applicationContext, this)
        val alarmSetter= itemAdapter.alarmSetter
        var isConnected=false

        // init the drop down menus
        val daySelect= findViewById<View>(R.id.dayEntry) as Spinner
        val slotSelect= findViewById<View>(R.id.slotEntry) as Spinner

        daySelect.adapter = itemAdapter.getDayAdapter()
        slotSelect.adapter = itemAdapter.getSlotAdapter()


        // set view for show time table
        val showBtn = findViewById<View>(R.id.showbtn) as Button
        showBtn.setOnClickListener{itemAdapter.buildShowOnclick()}

        //time picker
        val pickBtn=findViewById<View>(R.id.pickTime) as Button
        val topViewTime= Dialog(this)
        topViewTime.setContentView(R.layout.time_picker)

        pickBtn.setOnClickListener()
        {
            topViewTime.show()
        }

        //sync btn
        val syncBtn=findViewById<View>(R.id.syncBtn) as Button
        syncBtn.setOnClickListener()
        {
            alarmSetter.sync()
        }

        //set btn
        val setBtn=findViewById<View>(R.id.setBtn) as Button
        setBtn.setOnClickListener() //tested->OK
        {
            itemAdapter.alarmSetButtonEvent(topViewTime,daySelect.selectedItem as String, slotSelect.selectedItem as String)
        }

        //default button
        val defaultBtn=findViewById<View>(R.id.defaultBtn)
        defaultBtn.setOnClickListener()
        {
            alarmSetter.ensureDefault()
        }

        //status text
        val statusText=findViewById<View>(R.id.statusText) as TextView
        val handler=Handler(Looper.getMainLooper())
        val ipText=findViewById<View>(R.id.editTextDeviceIP) as EditText
        fun statusUpdater()
        {
            Thread{
                val h=ipText.text.toString()
                alarmSetter.hostUrl="http://$h/"
                val status = Connectivity.ping(alarmSetter.hostUrl)
                isConnected=(status=="Ready")
                println(isConnected)

            }.start()
            Thread.sleep(1000)
            if(isConnected)
            {
                statusText.text=getString(R.string.connected)
                statusText.setTextColor(Color.parseColor("#03fc66"))
            }
            else
            {
                statusText.text=getString(R.string.disconnected)
                statusText.setTextColor(Color.parseColor("#fc2c03"))
            }
        }
        statusUpdater()
        handler.postDelayed( {statusUpdater()},60000)
    }
}