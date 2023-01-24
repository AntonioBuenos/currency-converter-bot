package by.smirnov.currencyconverterbot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainCurrencies {
    USD("$"), EUR("€"), RUB("₽"), CNY("Ұ"), BYN("Br");

    private final String symbol;
}
