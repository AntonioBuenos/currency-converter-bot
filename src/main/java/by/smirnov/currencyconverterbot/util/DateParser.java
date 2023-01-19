package by.smirnov.currencyconverterbot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Slf4j
public class DateParser {

    private final String inputDateRegex = "(0?[1-9]|[12]\\d|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";
    public static final String INPUT_DATE_PATTERN = "d-M-yyyy";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(INPUT_DATE_PATTERN);

    public static LocalDate parseDate(String text) {
        String date = null;
        Pattern pattern = Pattern.compile(inputDateRegex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) date = matcher.group();
        return LocalDate.parse(date, formatter);
    }
}
