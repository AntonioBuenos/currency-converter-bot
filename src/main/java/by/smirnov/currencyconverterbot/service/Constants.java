package by.smirnov.currencyconverterbot.service;

public interface Constants {

    String COMMAND_SET_CURRENCY = "/set_currency";
    String COMMAND_START = "/start";
    String COMMAND_TODAY_RATES = "/today_rates";
    String COMMAND_HELP = "/help";
    String ORIGINAL = "ORIGINAL";
    String TARGET = "TARGET";
    String MESSAGE_CHOOSE_CURRENCIES = "Please choose Original and Target currencies";
    String FORMAT_RATES_RESPONSE = "%4.2f %s is %4.2f %s";
}
