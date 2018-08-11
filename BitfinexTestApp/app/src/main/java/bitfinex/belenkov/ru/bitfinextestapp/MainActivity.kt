package bitfinex.belenkov.ru.bitfinextestapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import bitfinex.belenkov.ru.bitfinextestapp.implementation.BitfinexWebSocket
import bitfinex.belenkov.ru.bitfinextestapp.implementation.BitfinexLineChartController
import bitfinex.belenkov.ru.bitfinextestapp.implementation.BitfinexTickerEmmiter
import com.github.mikephil.charting.data.Entry
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var chartController: BitfinexLineChartController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bitfinexWebSocket = BitfinexWebSocket(getString(R.string.url))
        var bitfinexTickerEmmiter = BitfinexTickerEmmiter(this)
        chartController = BitfinexLineChartController(this, chart)

        chartController.chartInit()

        if (savedInstanceState != null)
            chartController.refreshDataSets(savedInstanceState[getString(R.string.lowEntries)] as ArrayList<Entry>,
                    savedInstanceState[getString(R.string.highEntries)] as ArrayList<Entry>)

        bitfinexTickerEmmiter.initTickerSuscriber(chartController)

        bitfinexWebSocket
                .getWebSocket()
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            it.connect()
                            it.sendText(getUsdTickerTrading())
                            bitfinexTickerEmmiter
                                    .getBitfinexResponse(it)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(bitfinexTickerEmmiter.tickerSubscriber)
                        },
                        onError = {
                            Log.e(getString(R.string.TAG_ERROR), it.message)
                        }
                )
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putSerializable(getString(R.string.lowEntries), chartController.lowEntries)
        outState.putSerializable(getString(R.string.highEntries), chartController.highEntries)
        super.onSaveInstanceState(outState)
    }

    private fun getUsdTickerTrading(): String {
        val gson = Gson()
        val map = LinkedHashMap<String, String>()
        map["event"] = "subscribe"
        map["channel"] = "ticker"
        map["symbol"] = "tBTCUSD"
        return gson.toJson(map).toString()
    }
}
