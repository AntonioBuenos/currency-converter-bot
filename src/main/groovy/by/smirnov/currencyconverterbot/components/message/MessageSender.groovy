package by.smirnov.currencyconverterbot.components.message

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class MessageSender {

    static SendMessage sendMessage(Message message, String messageText, List<List<InlineKeyboardButton>> buttons) {
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .parseMode(ParseMode.HTML)
                .text(messageText)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build()
    }

    static SendMessage sendMessage(Message message, String messageText) {
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .parseMode(ParseMode.HTML)
                .text(messageText)
                .build()
    }
}
