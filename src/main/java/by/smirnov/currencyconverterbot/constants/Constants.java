package by.smirnov.currencyconverterbot.constants;

import java.time.LocalDate;

public interface Constants {

    String ORIGINAL = "ORIGINAL";
    String TARGET = "TARGET";
    String MESSAGE_CHOOSE_CURRENCIES = "Please choose Original and Target currencies";
    String FORMAT_RATES_RESPONSE = "%4.2f %s is %4.2f %s";
    String ERROR = "Error occurred: {}";
    String COMMAND_LIST_INIT_ERROR = "Error setting bot's command list: {}";
    String CONVERSION_ERROR = "Error of conversion method: {}";
    String EDIT_MESSAGE_ERROR = "Error of editing message text method: {}";
    String PARSE_ERROR = "Parsing error occurred: {}";
    String CONVERSION_NULL_ERROR = "Error of conversion method (some data is null): {}";
    String MESSAGE_BAD_COMMAND = "Command not recognized!";
    String MESSAGE_START = "Вас приветствует бот курсов валют! Используйте меню команд.";
    String DELIM = ":";
    String COMMAND_KEY = "bot_command";
    LocalDate TODAY = LocalDate.now();
    LocalDate TOMORROW = TODAY.plusDays(1);
}
