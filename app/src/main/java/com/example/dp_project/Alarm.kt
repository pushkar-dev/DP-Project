package com.example.dp_project


import android.content.Context
import android.provider.AlarmClock
import java.io.File


class Alarm(private var ctx: Context, private val filename: String = "alarmDump.txt") {
    private val clock:AlarmClock = AlarmClock()
    var timeTable: Array<IntArray> =Array(7){IntArray(3)}
    private var timeTableDefault: Array<IntArray> =Array(7){IntArray(3)}
    val hostUrl="http://192.168.43.243/"

    private val dayOfWeek: Array<String> =arrayOf("mon","tue","wed","thu","fri","sat","sun")

    private fun fetch():Array<IntArray> // fetch data from file dump
    {
        val f= File(this.ctx.filesDir, this.filename)
        val s:String=f.readText()
        val res:Array<IntArray> =Array(7){IntArray(3)}
        for((i, line) in s.trim().split("\n").withIndex())
        {
            for((j,istr) in line.trim().split(" ").withIndex())
            {
                res[i][j]=istr.toInt()
                //println(istr.toFloat())
            }
        }
        return res
    }

    private fun setDefaultTimetable()
    {
        this.timeTable=this.timeTableDefault
    }

    init {
        val f = File(this.ctx.filesDir, this.filename)
        arrayOf(
            intArrayOf(800, 1400, 1900),
            intArrayOf(800, 1400, 1900),
            intArrayOf(800, 1400, 1900),
            intArrayOf(800, 1400, 1900),
            intArrayOf(800, 1400, 1900),
            intArrayOf(800, 1400, 1900),
            intArrayOf(800, 1400, 1900),
        ).also { timeTableDefault = it }

        if (f.exists()) { // if file  exists then fetch data from there
            fetch().also { timeTable = it }
        } else { // else reset data to default
            setDefaultTimetable()
        }
    }



    fun dump() // dump input data to file
    {
        val f= File(this.ctx.filesDir,this.filename)
        var s=""
        for( i in 0..6)
        {
            for(j in 0..2)
            {
                s+=timeTable[i][j].toString()+" "
            }
            s+="\n"
        }
        f.writeText(s)
    }


    fun sync() // posts same data to esp32 web server
    {
        var res:String?
        var req:String?="{"
        for((i,row) in this.timeTable.withIndex())
        {
            req+="\""+this.dayOfWeek[i]+"\":["
            for((j,value) in row.withIndex())
            {
                req+=value.toString()
                if(j!=2) req+=","
            }
            req+="]"
            if(i!=6) req+=","
        }
        req+="}"
        // all network operations must not be done in main thread
        Thread{
            res=Connectivity.ping(hostUrl)
            if(res=="Ready")
            {
                res=Connectivity.send(hostUrl,"data",req)
                println(res) //only for debugging purpose
            }
            else
            {
                println("Host not up, kindly crosscheck the ip or device power!")
            }
        }.start()
    }

    fun ensureDefault()
    {
        this.setDefaultTimetable()
        this.dump()
        // if they were to set alarm set here
    }

    fun setAlarm()// sets alarm for all times in float array
    {
        // sets alarm for all times in float array
    }

}