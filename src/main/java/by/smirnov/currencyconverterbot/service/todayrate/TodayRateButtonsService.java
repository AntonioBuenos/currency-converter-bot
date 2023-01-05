package by.smirnov.currencyconverterbot.service.todayrate;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TodayRateButtonsService {

    SendMessage getButtons(Long chatId);
}
