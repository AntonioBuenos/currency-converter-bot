package by.smirnov.currencyconverterbot.constants;

import java.time.LocalDate;

public interface Constants {

    String ORIGINAL = "ORIGINAL";
    String TARGET = "TARGET";
    String MESSAGE_UNDER_CONSTRUCTION = "Данный функционал появится в ближайшее время";
    String MESSAGE_CHOOSE_CURRENCIES = "Выберите конвертируемую (слева) и целевую (справа) валюты";
    String FORMAT_RATES_RESPONSE = "%4.2f %s is %4.2f %s";
    String MESSAGE_BAD_COMMAND = "Команда не опознана!";
    String MESSAGE_INPUT_DATE = "На какую дату предоставить курсы валют? \nВведите в формате '31.12.2022'";
    String MESSAGE_START = "Вас приветствует бот курсов валют! Используйте меню команд.";
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
    String DAYLY_RATE_TYPE_MESSAGE = "Выберите курсы основных валют или курсы всех валют, устанавливаемых НБРБ на %s:";
}
