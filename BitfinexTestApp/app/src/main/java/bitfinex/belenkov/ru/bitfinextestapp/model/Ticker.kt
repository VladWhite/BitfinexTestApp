package bitfinex.belenkov.ru.bitfinextestapp.model

class Ticker(fields: HashMap<Int, Float>) {
    var channel_id = 0f
    var bid = 0f
    var biz_size = 0f
    var ask = 0f
    var ask_size = 0f
    var daily_change = 0f
    var daily_change_perc = 0f
    var last_price = 0f
    var volume = 0f
    var high = 0f
    var low = 0f

    init {
        channel_id = fields[0]!!
        bid = fields[1]!!
        biz_size = fields[2]!!
        ask = fields[3]!!
        ask_size = fields[4]!!
        daily_change = fields.get(key = 5)!!
        daily_change_perc = fields[6]!!
        last_price = fields[7]!!
        volume = fields[8]!!
        high = fields[9]!!
        low = fields[10]!!
    }

}