package by.smirnov.currencyconverterbot.constants;

import java.time.LocalDate;

public interface Constants {

    String ORIGINAL = "ORIGINAL";
    String TARGET = "TARGET";
    String MESSAGE_UNDER_CONSTRUCTION = "Данный функционал появится в ближайшее время";
    String MESSAGE_CHOOSE_CURRENCIES = "Please choose Original and Target currencies";
    String FORMAT_RATES_RESPONSE = "%4.2f %s is %4.2f %s";
    String MESSAGE_BAD_COMMAND = "Команда не опознана!";
    String MESSAGE_INPUT_DATE = "На какую дату предоставить курсы валют? \nВведите в формате '31.12.2022'";
    String MESSAGE_START = "Вас приветствует бот курсов валют! Используйте меню команд.";
    String DELIM = ":";
    String COMMAND_KEY = "bot_command";
    LocalDate TODAY = LocalDate.now();
    LocalDate TOMORROW = TODAY.plusDays(1);
}
