package by.smirnov.currencyconverterbot.service.commands;

import lombok.Getter;

@Getter
public enum Commands {

    START("/start", "Стартовая информация о боте"),
    SET_CURRENCY("/set_currency", "Конвертер сумм в основных валютах"),
    TODAY_RATES("/today_rates", "Курсы на сегодня"),
    TOMORROW_RATES("/tomorrow_rates", "Курсы на завтра"),
    SPAM("/spam", "Отправить сообщения всем пользователям"),
    HELP("/help", "Общая информация о боте");

    private final String cmd;
    private final String message;

    Commands(String cmd, String message) {
        this.cmd = cmd;
        this.message = message;
    }

    public boolean equals(String cmd){
        return this.getCmd().equals(cmd);
    }
}
