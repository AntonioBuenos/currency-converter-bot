package by.smirnov.currencyconverterbot.components.commands

enum Commands {

    START("/start", "Стартовая информация о боте"),
    RATES_BY_DATE("/by_date_rates", "Курсы на дату"),
    RATES_TODAY("/today_rates", "Курсы на сегодня"),
    RATES_TOMORROW("/tomorrow_rates", "Курсы на завтра"),
    SET_CURRENCY("/set_currency", "Конвертер сумм в основных валютах"),
    SPAM("/spam", "Отправить сообщения всем пользователям"),
    UPD_CURRENCIES("/upd_currencies", "Загрузить обновление перечня валют с НБРБ"),
    HELP("/help", "Общая информация о боте");

    final String cmd
    final String message

    Commands(String cmd, String message) {
        this.cmd = cmd
        this.message = message
    }

    static Commands findByCmd(String cmd){
        return Arrays.stream(values())
                .filter { it.cmd == cmd }
                .findFirst()
                .orElse(null)
    }
}