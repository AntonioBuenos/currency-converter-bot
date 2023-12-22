package by.smirnov.currencyconverterbot.util

import lombok.experimental.UtilityClass

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@UtilityClass
class DateFormatter {

    private static final String DATE_PATTERN = "d MMMM yyyy"
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            DATE_PATTERN,
            Locale.of("ru", "RU"))

    static String formatDate(LocalDate date) {
        date.format(FORMATTER)
    }
}
