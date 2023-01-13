package by.smirnov.currencyconverterbot.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;

public interface DailyRateButtonsService {

    SendMessage getButtons(Long chatId, LocalDate date);
}
