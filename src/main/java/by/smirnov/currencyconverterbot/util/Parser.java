package by.smirnov.currencyconverterbot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static by.smirnov.currencyconverterbot.constants.LogConstants.PARSE_ERROR;

@UtilityClass
@Slf4j
public class Parser {

    public static Double parseDouble(String messageText) {
        try {
            return Double.parseDouble(messageText);
        } catch (NullPointerException | NumberFormatException e) {
            log.error(PARSE_ERROR, e.getMessage());
            return null;
        }
    }
}
