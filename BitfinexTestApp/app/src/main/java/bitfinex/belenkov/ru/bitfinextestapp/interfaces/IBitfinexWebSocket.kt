package bitfinex.belenkov.ru.bitfinextestapp.interfaces

import com.neovisionaries.ws.client.WebSocket
import io.reactivex.Observable

/**
 * autor - Belenkov Vladislav
 * Здесь происходит генерация вебсокета
 * */
interface IBitfinexWebSocket {
    fun getWebSocket(): Observable<WebSocket>
}