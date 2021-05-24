package com.intensecoder.mpchartstutorial

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener


private const val TAG = "BarChartClick"

private const val MIN_VALUE = -9f
private const val MAX_VALUE = 9f

class BarChartClickActivity : AppCompatActivity(), OnChartGestureListener {
    private lateinit var barChart: BarChart

    private var temperatureList = ArrayList<Temperature>()
    private var barModelList = ArrayList<BarModel>()


    private var fixedCenterYPoint = 800.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_click)

        barChart = findViewById(R.id.barChart)

        temperatureList = getTemperatureList()

        initBarChart()

        setDataForBarChart()

    }

    private fun initBarChart() {
        val xAxis = barChart.xAxis
        val yAxisL = barChart.axisLeft
        val yAxisR = barChart.axisRight

        xAxis.granularity = 1f
        yAxisL.granularity = 1f
        yAxisL.axisMinimum = MIN_VALUE // start at zero
        yAxisL.axisMaximum = MAX_VALUE
        yAxisR.isEnabled = false
        barChart.setDrawValueAboveBar(true)
        barChart.setFitBars(true)
        barChart.isDragEnabled = false
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyAxisFormatter()
        yAxisL.setDrawGridLines(true)
        val description: Description = barChart.description
        description.isEnabled = false

        val legend: Legend = barChart.legend
        legend.isEnabled = true

        // disable zoom , drag and highlight of bar
        barChart.highlightValues(null)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.isDragEnabled = true
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)


        //add listener
        barChart.onChartGestureListener = this
    }

    private fun setDataForBarChart() {

        //you can replace this data object with  your custom object
        for (i in temperatureList.indices) {
            val temperature = temperatureList[i]
            barModelList.add(BarModel(i.toFloat(), temperature.temperatureVal))
        }


        drawBarChart(barModelList)

    }


    // to draw or redraw graph
    private fun drawBarChart(barModelList: ArrayList<BarModel>) {
        val barEntries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()

        for (barModel in barModelList) {
            barEntries.add(BarEntry(barModel.xValue, barModel.yValue))
            // specific colors
            if (barModel.yValue >= 0)
                colors.add(Color.GREEN)
            else
                colors.add(Color.RED)
        }

        val barDataSet = BarDataSet(barEntries, "")
        barDataSet.colors = colors
        barDataSet.setDrawValues(false)
        barDataSet.highLightAlpha = 0

        val theData = BarData(barDataSet)
        theData.setDrawValues(false)
        barChart.data = theData
        barChart.invalidate()

    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < temperatureList.size) {
                temperatureList[index].day
            } else {
                ""
            }
        }
    }


    // simulate api call
    // we are initialising it directly
    private fun getTemperatureList(): ArrayList<Temperature> {
        temperatureList.add(Temperature("Mon", 3f))
        temperatureList.add(Temperature("Tue", 7f))
        temperatureList.add(Temperature("Wed", -5f))
        temperatureList.add(Temperature("Thu", -3f))
        temperatureList.add(Temperature("Fri", 9f))
        temperatureList.add(Temperature("Sat", 2f))

        return temperatureList
    }


    // change chart values on click
    // increase or decrease graph bars
    private fun incrementAndDecrement(yPoint: Float, entry: Entry, incDecVal: Float) {
        //check pos click
        var position = -1
        for (i in temperatureList.indices) {
            val data = barModelList[i]
            if (data.xValue == entry.x) {
                position = i
            }
        }

        if (position == -1) {
            return
        }

        if (yPoint >= fixedCenterYPoint) {
            //decrement
            if (barModelList[position].yValue <= MIN_VALUE) {
                return
            }
            barModelList[position].yValue -= incDecVal
        } else {
            //increment
            if (barModelList[position].yValue >= MAX_VALUE) {
                return
            }
            barModelList[position].yValue += incDecVal
        }
    }


    /*
    *
    * Chart Gesture Listeners
    * */

    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
    }

    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
    }

    override fun onChartLongPressed(me: MotionEvent?) {
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
        Log.d(TAG, "onChartSingleTapped: called")

        me?.let { motionEvent ->
            // getting the particular selected bar
            val e: Entry? = barChart.getEntryByTouchPoint(motionEvent.x, motionEvent.y)
            e?.let { entry ->
                incrementAndDecrement(motionEvent.y, entry, 1f)
                drawBarChart(barModelList)
            }
        }
    }


    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
    }


}