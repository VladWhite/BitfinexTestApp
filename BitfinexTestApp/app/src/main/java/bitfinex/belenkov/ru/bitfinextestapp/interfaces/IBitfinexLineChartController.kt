package bitfinex.belenkov.ru.bitfinextestapp.interfaces

import bitfinex.belenkov.ru.bitfinextestapp.model.Ticker
import com.github.mikephil.charting.data.Entry
/**
 * autor - Belenkov Vladislav
 * Контроллер, отвечающий за инциализацию и обновления графика,
 * отображающего биткоин
 * */
interface IBitfinexLineChartController {
    fun chartInit()
    fun updateChart(ticker: Ticker)
    fun refreshDataSets(lowEntries:ArrayList<Entry>, highEntries:ArrayList<Entry>)
}