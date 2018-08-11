package bitfinex.belenkov.ru.bitfinextestapp.implementation


import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Test
import java.util.concurrent.TimeUnit


class BitfinexTickerEmmiterTest {

    @Test
    fun webSocketTest() {
        val bitfinexWs = BitfinexWebSocket("wss://api.bitfinex.com/ws")
        val testWsObserver = bitfinexWs.getWebSocket().test()

        testWsObserver
                .assertNoErrors()
                .assertValue { true }
    }

    @Test
    fun tickerEmmiterTest() {
        val bitfinexWs = BitfinexWebSocket("wss://api.bitfinex.com/ws")
        bitfinexWs.getWebSocket()
                .subscribeBy(
                        onNext = {
                            it.connect()
                            it.sendText(getUsdTickerTrading())
                            val bitfinexTickerEmmiter = BitfinexTickerEmmiter(AppCompatActivity())
                            bitfinexTickerEmmiter
                                    .getBitfinexResponse(it)
                                    .test()
                                    .awaitDone(5, TimeUnit.SECONDS)
                                    .assertValue { true }
                        }
                )
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