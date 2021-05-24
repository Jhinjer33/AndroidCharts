package com.intensecoder.mpchartstutorial

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils


class LineChartActivity : AppCompatActivity() {


    private lateinit var lineChart: LineChart
    private var scoreList = ArrayList<Score>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)


        lineChart = findViewById(R.id.lineChart)
        
        scoreList = getScoreList()

        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()


        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            entries.add(Entry(i.toFloat(), score.score.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = LineData(lineDataSet)
        lineChart.data = data

        lineChart.invalidate()

    }

    // simulate api call
    // we are initialising it directly
    private fun getScoreList(): ArrayList<Score> {
        scoreList.add(Score("John", 56))
        scoreList.add(Score("Rey", 75))
        scoreList.add(Score("Steve", 85))
        scoreList.add(Score("Kevin", 45))
        scoreList.add(Score("Jeff", 63))

        return scoreList
    }


}