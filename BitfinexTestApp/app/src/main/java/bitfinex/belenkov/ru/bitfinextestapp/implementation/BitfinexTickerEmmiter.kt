package bitfinex.belenkov.ru.bitfinextestapp.implementation

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import bitfinex.belenkov.ru.bitfinextestapp.interfaces.IBitfinexTickerEmmiter
import bitfinex.belenkov.ru.bitfinextestapp.model.Ticker
import bitfinex.belenkov.ru.bitfinextestapp.R
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFrame
import io.reactivex.Observable

import io.reactivex.observers.DisposableObserver


class BitfinexTickerEmmiter(private val activity: AppCompatActivity) : IBitfinexTickerEmmiter {

    private lateinit var responseFields: List<String>
    private lateinit var fieldsMap: HashMap<Int, Float>
    private lateinit var res: String
    lateinit var tickerSubscriber: DisposableObserver<Ticker>



    override fun getBitfinexResponse(wss: WebSocket): Observable<Ticker> {
        return Observable.create { subscriber ->
            run {
                wss.addListener(object : WebSocketAdapter() {
                    override fun onTextMessage(websocket: WebSocket?, text: String?) {
                        super.onTextMessage(websocket, text)
                        if (!text!!.contains("hb") && !text.contains("event")) {
                            subscriber.onNext(getTicker(text))
                        }
                    }

                    override fun onTextMessageError(websocket: WebSocket?, cause: WebSocketException?, data: ByteArray?) {
                        super.onTextMessageError(websocket, cause, data)
                        Toast.makeText(activity, activity.getString(R.string.communicationError) + cause, Toast.LENGTH_LONG).show()
                        subscriber.onError(cause!!)
                    }

                    override fun onDisconnected(websocket: WebSocket?, serverCloseFrame: WebSocketFrame?, clientCloseFrame: WebSocketFrame?, closedByServer: Boolean) {
                        subscriber.onComplete()
                    }
                })
            }
        }
    }

   fun initTickerSuscriber(chartController: BitfinexLineChartController) {
        tickerSubscriber = object : DisposableObserver<Ticker>() {
            override fun onComplete() {
                Toast.makeText(activity, activity.getString(R.string.endTicker), Toast.LENGTH_LONG).show()
            }

            override fun onNext(t: Ticker) {
                chartController.updateChart(t)
            }

            override fun onError(e: Throwable) {
                Toast.makeText(activity, activity.getString(R.string.errorTicker) + e, Toast.LENGTH_LONG).show()
            }

        }
    }

   private fun getTicker(response: String): Ticker {
        responseFields = response.split(",")
        fieldsMap = HashMap()

        for (i in responseFields.indices) {
            res = responseFields[i]

            if (res.startsWith("[")) res = res.replace("[", "")
            if (res.endsWith("]")) res = res.replace("]", "")

            fieldsMap[i] = res.toFloat()
        }

        return Ticker(fieldsMap)
    }
}
