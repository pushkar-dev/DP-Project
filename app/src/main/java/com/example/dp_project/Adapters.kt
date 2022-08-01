package com.example.dp_project

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi

class Adapters(appCtx:Context, ctx:Context) {
    val alarmSetter:Alarm
    private val ctx:Context

    init {
        alarmSetter = Alarm(appCtx)
        this.ctx=ctx
    }

    fun getDayAdapter():ArrayAdapter<CharSequence> //returns adapter for day spinner
    {
        val adapter = ArrayAdapter.createFromResource(
            ctx,
            R.array.Days,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        return adapter
    }
    fun getSlotAdapter():ArrayAdapter<CharSequence> //returns adapter for slot spinner
    {
        val adapter = ArrayAdapter.createFromResource(
            ctx,
            R.array.Slot,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        return adapter
    }
    private fun wrongSelectAlertBuilder():AlertDialog.Builder //returns alert builder for wrong time selection
    {
        val wrongAlarmAlert=AlertDialog.Builder(ctx)
        wrongAlarmAlert.setTitle(R.string.wrongSelectAlert)
        wrongAlarmAlert.setMessage(R.string.wrongSelectMsg)
        return wrongAlarmAlert
    }

    fun buildShowOnclick() //event handler for show button
    {
        val tableView = Dialog(ctx)
        tableView.setContentView(R.layout.dialog_tt)
        val table=tableView.findViewById<View>(R.id.ttView) as TableLayout
        for(i in 2..8)
        {
            val row=table.getChildAt(i) as TableRow
            for(j in 1..3)
            {
                val cell=row.getChildAt(j) as TextView

                cell.text=alarmSetter.timeTable[i-2][j-1].toString()
            }
        }
        tableView.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun alarmSetButtonEvent(topViewTime:Dialog, selectedDay:String, selectedSlot:String) //event handler for set button
    {
        val t=topViewTime.findViewById<View>(R.id.timePicker1) as TimePicker
        println("Hour=${t.hour}")
        println("Minute=${t.minute}")
        val i= getDayAdapter().getPosition(selectedDay)
        val j= getSlotAdapter().getPosition(selectedSlot)
        val value:Int= (t.hour*100+t.minute)
        if(i>0 && j>0)
        {
            alarmSetter.timeTable[i-1][j-1]=value
        }
        else
        {
            val alertDialog=wrongSelectAlertBuilder()
            alertDialog.show()
        }
        println("slot=${selectedDay}")
        println("day=${selectedSlot}") // for debugging purposes only
        println("i=${i}, j=${j}, value=${value}")
        alarmSetter.dump()
    }
}