package by.smirnov.currencyconverterbot.constants;

import java.time.LocalDate;

public interface CommonConstants {
    String ORIGINAL = "ORIGINAL";
    String TARGET = "TARGET";
    String FORMAT_RATES_RESPONSE = "%4.2f %s is %4.2f %s";
    String DELIM = ":";
    String COMMAND_KEY = "bot_command";
    LocalDate TODAY = LocalDate.now();
    LocalDate TOMORROW = TODAY.plusDays(1);
    String MAIN_CURRENCIES = "main_currencies";
    String ALL_CURRENCIES = "all_currencies";
    String MAIN_CURRENCIES_NAME = "Основные валюты";
    String ALL_CURRENCIES_NAME = "Все валюты";
}
