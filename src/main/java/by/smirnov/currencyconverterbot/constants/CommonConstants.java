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
    String TODAY_MAIN_CURRENCIES = "today_main_currencies";
    String TOMORROW_MAIN_CURRENCIES = "tomorrow_main_currencies";
    String ALL_CURRENCIES = "all_currencies";
    String TODAY_ALL_CURRENCIES = "today_all_currencies";
    String TOMORROW_ALL_CURRENCIES = "tomorrow_all_currencies";
    String MAIN_CURRENCIES_NAME = "Основные валюты";
    String ALL_CURRENCIES_NAME = "Все валюты";
}
