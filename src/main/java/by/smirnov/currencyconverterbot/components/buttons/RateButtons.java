package by.smirnov.currencyconverterbot.components.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;

public interface RateButtons {

    SendMessage getButtons(Long chatId, LocalDate date);
}
