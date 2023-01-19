package by.smirnov.currencyconverterbot.components.message;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class MessageSender {

    public SendMessage sendMessage(Message message, String messageText, List<List<InlineKeyboardButton>> buttons) {
        return SendMessage.builder()
                .text(messageText)
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    public SendMessage sendMessage(Message message, String messageText) {
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(messageText)
                .build();
    }
}
