package by.smirnov.currencyconverterbot.constants;

import java.time.LocalDate;

public interface Constants {

    String ORIGINAL = "ORIGINAL";
    String TARGET = "TARGET";
    String MESSAGE_CHOOSE_CURRENCIES = "Please choose Original and Target currencies";
    String FORMAT_RATES_RESPONSE = "%4.2f %s is %4.2f %s";
    String MESSAGE_BAD_COMMAND = "Command not recognized!";
    String MESSAGE_START = "Вас приветствует бот курсов валют! Используйте меню команд.";
    String DELIM = ":";
    String COMMAND_KEY = "bot_command";
    LocalDate TODAY = LocalDate.now();
    LocalDate TOMORROW = TODAY.plusDays(1);
}
