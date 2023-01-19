package by.smirnov.currencyconverterbot.components.commands;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum Commands {

    START("/start", "Стартовая информация о боте"),
    SET_CURRENCY("/set_currency", "Конвертер сумм в основных валютах"),
    RATES_TODAY("/today_rates", "Курсы на сегодня"),
    RATES_TOMORROW("/tomorrow_rates", "Курсы на завтра"),
    RATES_BY_DATE("/by_date_rates", "Курсы на дату"),
    SPAM("/spam", "Отправить сообщения всем пользователям"),
    UPD_CURRENCIES("/upd_currencies", "Загрузить обновление перечня валют с НБРБ"),
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

    public static Commands findByCmd(String cmd){
        return Arrays.stream(Commands.values())
                .filter(v -> Objects.equals(v.getCmd(), cmd))
                .findFirst()
                .orElse(null);
    }

}
