package bitfinex.belenkov.ru.bitfinextestapp.implementation

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import bitfinex.belenkov.ru.bitfinextestapp.interfaces.IBitfinexLineChartController
import bitfinex.belenkov.ru.bitfinextestapp.model.Ticker
import bitfinex.belenkov.ru.bitfinextestapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.io.Serializable

class BitfinexLineChartController(
        private val context: AppCompatActivity,
        private val lineChart: LineChart,
        private var entriesCount: Float = 0f,
        var lowEntries: ArrayList<Entry> = ArrayList(),
        var highEntries: ArrayList<Entry> = ArrayList(),
        var lowDataSet: LineDataSet = LineDataSet(lowEntries, context.getString(R.string.tickerLow)),
        var highDataSet: LineDataSet = LineDataSet(highEntries, context.getString(R.string.tickerHigh)),
        private val lineData: LineData = LineData(lowDataSet, highDataSet)) : IBitfinexLineChartController, Serializable {

    override fun chartInit() {
        lowEntries.add(Entry(0f, 7000f))
        highEntries.add(Entry(0f, 7000f))
        lineChart.data = lineData
        lineChart.xAxis.axisMinimum = 1f
        lineChart.xAxis.axisMaximum = 30f
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.isGranularityEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setTouchEnabled(true)
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.valueFormatter = IAxisValueFormatter { value, axis -> context.getString(R.string.usd) + value }

        lowDataSet.lineWidth = 3f
        lowDataSet.color = Color.BLUE
        highDataSet.lineWidth = 3f
        highDataSet.color = Color.RED
    }

    override fun updateChart(ticker: Ticker) {
        entriesCount = lineData.entryCount.toFloat()
        this.lowEntries.add(Entry(entriesCount, ticker.low))
        this.highEntries.add(Entry(entriesCount, ticker.high))
        lowDataSet.notifyDataSetChanged()
        highDataSet.notifyDataSetChanged()
        lineData.notifyDataChanged()
        lineChart.notifyDataSetChanged()

        lineChart.moveViewToX(lineData.entryCount.toFloat())
    }

    override fun refreshDataSets(lowEntries: ArrayList<Entry>, highEntries: ArrayList<Entry>) {
        this.lowEntries.addAll(lowEntries)
        this.highEntries.addAll(highEntries)

        lowDataSet.notifyDataSetChanged()
        highDataSet.notifyDataSetChanged()
        lineData.notifyDataChanged()
        lineChart.notifyDataSetChanged()

        lineChart.moveViewToX(lineData.entryCount.toFloat())
    }
}