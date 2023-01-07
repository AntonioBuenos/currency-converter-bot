package by.smirnov.currencyconverterbot.constants;

public interface Constants {

    String COMMAND_SET_CURRENCY = "/set_currency";
    String COMMAND_START = "/start";
    String COMMAND_TODAY_RATES = "/today_rates";
    String COMMAND_HELP = "/help";
    String ORIGINAL = "ORIGINAL";
    String TARGET = "TARGET";
    String MESSAGE_CHOOSE_CURRENCIES = "Please choose Original and Target currencies";
    String FORMAT_RATES_RESPONSE = "%4.2f %s is %4.2f %s";
    String ERROR = "Error occurred: {}";
    String COMMAND_LIST_INIT_ERROR = "Error setting bot's command list: {}";
    String CONVERSION_ERROR = "Error of conversion method: {}";
    String EDIT_MESSAGE_ERROR = "Error of editing message text method: {}";
    String MESSAGE_BAD_COMMAND = "Command not recognized!";
    String MESSAGE_START = "Вас приветствует бот курсов валют! Используйте меню команд.";
    String DELIM = ":";
}
