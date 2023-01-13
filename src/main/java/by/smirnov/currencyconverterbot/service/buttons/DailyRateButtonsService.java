package by.smirnov.currencyconverterbot.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface DailyRateButtonsService {

    SendMessage getButtons(Long chatId);
}
