package by.smirnov.currencyconverterbot.util

import groovy.util.logging.Slf4j
import lombok.experimental.UtilityClass

import static by.smirnov.currencyconverterbot.constants.LogConstants.PARSE_ERROR


@UtilityClass
@Slf4j
class DoubleParser {

    static Double parse(String messageText) {
        try {
            return Double.parseDouble(messageText)
        } catch (NullPointerException | NumberFormatException e) {
            log.error(PARSE_ERROR, e.getMessage())
            return null
        }
    }
}
