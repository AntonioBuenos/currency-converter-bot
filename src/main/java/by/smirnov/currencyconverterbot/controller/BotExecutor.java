package by.smirnov.currencyconverterbot.controller;

import by.smirnov.currencyconverterbot.components.message.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static by.smirnov.currencyconverterbot.constants.LogConstants.EDIT_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.LogConstants.EXECUTE_EDIT_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.LogConstants.EXECUTE_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.LogConstants.EXECUTE_SEND_MESSAGE_ERROR;

@Component
@Slf4j
public class BotExecutor {

    private final TelegramBot bot;
    private final MessageSender messageSender;

    public BotExecutor(@Lazy TelegramBot bot, MessageSender messageSender) {
        this.bot = bot;
        this.messageSender = messageSender;
    }

    public void editMessage(String text, long chatId, int messageId) {
        try {
            bot.execute(EditMessageText.builder()
                    .chatId(chatId)
                    .parseMode(ParseMode.HTML)
                    .text(text)
                    .messageId(messageId)
                    .build());
        } catch (TelegramApiException e) {
            log.error(EDIT_MESSAGE_ERROR, e.getMessage());
        }
    }

    public void executeMessage(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(EXECUTE_SEND_MESSAGE_ERROR, e.getMessage());
        }
    }

    public void executeMessage(Message message, String messageText) {
        try {
            bot.execute(messageSender.sendMessage(message, messageText));
        } catch (TelegramApiException e) {
            log.error(EXECUTE_MESSAGE_ERROR, e.getMessage());
        }
    }

    public void executeMessage(EditMessageReplyMarkup message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(EXECUTE_EDIT_MESSAGE_ERROR, e.getMessage());
        }
    }
}
