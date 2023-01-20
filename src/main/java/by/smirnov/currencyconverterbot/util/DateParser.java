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

    private static final String INPUT_DATE_REGEX = "(0?[1-9]|[12]\\d|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)";
    private static final String FORMATTED_DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String INPUT_DATE_PATTERN = "d.M.yyyy";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(INPUT_DATE_PATTERN);

    public static LocalDate parseDate(String text) {
        String date = extractDate(text, INPUT_DATE_REGEX);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate parseFormattedDate(String text) {
        String date = extractDate(text, FORMATTED_DATE_REGEX);
        return LocalDate.parse(date);
    }

    private String extractDate(String text, String regexPattern){
        String date = "";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) date = matcher.group();
        return date;
    }
}
