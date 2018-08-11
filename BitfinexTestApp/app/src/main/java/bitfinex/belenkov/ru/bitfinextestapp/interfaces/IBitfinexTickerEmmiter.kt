package bitfinex.belenkov.ru.bitfinextestapp.interfaces

import bitfinex.belenkov.ru.bitfinextestapp.model.Ticker
import com.neovisionaries.ws.client.WebSocket
import io.reactivex.Observable


/**
 * autor - Belenkov Vladislav
 * Эммитер данных, на вход принимает ответ от вебсокета Bitfinex,
 * на выход отдает сгенерированный объект Ticker
 * */
interface IBitfinexTickerEmmiter {
    fun getBitfinexResponse(wss: WebSocket): Observable<Ticker>
}