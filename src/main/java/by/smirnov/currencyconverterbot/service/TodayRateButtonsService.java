package by.smirnov.currencyconverterbot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TodayRateButtonsService {

    SendMessage getTodayRateButtons(Long chatId);
}
