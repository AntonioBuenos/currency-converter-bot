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

    private String regex = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((18|19|20|21)\\d\\d)";
    public static final String DATE_PATTERN = "d-M-yyyy";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static LocalDate parseDate(String text) {
        String date = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) date = matcher.group();
        return LocalDate.parse(date, formatter);
    }

    public static void main(String[] args) {
        System.out.println(parseDate("31-12-2022"));
    }
}
