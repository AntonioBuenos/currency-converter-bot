package by.smirnov.currencyconverterbot.entity

import groovy.transform.TupleConstructor

@TupleConstructor
enum MainCurrencies {

    USD('$'), EUR("€"), RUB("₽"), CNY("Ұ"), BYN("Br")

    final String symbol

}