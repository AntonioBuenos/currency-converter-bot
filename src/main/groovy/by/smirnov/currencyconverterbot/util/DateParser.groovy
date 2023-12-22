package by.smirnov.currencyconverterbot.util

import groovy.util.logging.Slf4j
import lombok.experimental.UtilityClass

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@UtilityClass
@Slf4j
class DateParser {

    private static final String INPUT_DATE_REGEX = "(0?[1-9]|[12]\\d|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)"
    private static final String FORMATTED_DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}"
    private static final String INPUT_DATE_PATTERN = "d.M.yyyy"
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(INPUT_DATE_PATTERN)

    static LocalDate parse(String text) {
        String date = extractDate(text, INPUT_DATE_REGEX)
        LocalDate.parse(date, FORMATTER) //add error handling
    }

    static LocalDate parseFormattedDate(String text) {
        String date = extractDate(text, FORMATTED_DATE_REGEX)
        LocalDate.parse(date) //add error handling
    }

    private static String extractDate(String text, String regexPattern){
        def matcher = text =~ regexPattern
        if (matcher.find()) return matcher.group()
        return null
    }
}
