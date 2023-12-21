package by.smirnov.currencyconverterbot.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class DateFormatter {

    public static final String DATE_PATTERN = "d MMMM yyyy";

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.of("ru", "RU"));
        return date.format(formatter);
    }
}
