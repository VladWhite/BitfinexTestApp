package bitfinex.belenkov.ru.bitfinextestapp.implementation

import bitfinex.belenkov.ru.bitfinextestapp.interfaces.IBitfinexWebSocket
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketFactory
import io.reactivex.Observable
import java.io.Serializable

class BitfinexWebSocket(private val url: String) : IBitfinexWebSocket, Serializable {
    override fun getWebSocket(): Observable<WebSocket> {
        return Observable.create{subscriber->
            run{
                var wsf = WebSocketFactory()
                var ws = wsf.createSocket(url)
                subscriber.onNext(ws)
            }}
    }
}