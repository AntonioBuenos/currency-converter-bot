package by.smirnov.currencyconverterbot.constants;

public interface LogConstants {

    String ERROR = "Error occurred: {}";
    String EXECUTE_SEND_MESSAGE_ERROR = "Error occurred while executing SendMessage: {}";
    String EXECUTE_MESSAGE_ERROR = "Error occurred while executing Message: {}";
    String EXECUTE_EDIT_MESSAGE_ERROR = "Error occurred while executing EditMessageReplyMarkup: {}";
    String COMMAND_LIST_INIT_ERROR = "Error setting bot's command list: {}";
    String EDIT_MESSAGE_ERROR = "Error of editing message text method: {}";
    String PARSE_ERROR = "Parsing error occurred: {}";
    String CONVERSION_NULL_ERROR = "Error of conversion method (some data is null): {}";
    String GET_RATES_ERROR = "Error occurred while connection and getting rates from NBRB url: {}";
    String GET_CURRENCIES_ERROR = "Error occurred while connection and getting currencies from NBRB url: {}";
    String LOG_NEW_USER = "New user {} registered";
}
