package by.smirnov.currencyconverterbot.controller;

import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.controller.handlers.CallbackHandler;
import by.smirnov.currencyconverterbot.controller.handlers.CommandHandler;
import by.smirnov.currencyconverterbot.controller.handlers.TextMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.COMMAND_KEY;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CallbackHandler callbackHandler;
    private final CommandHandler commandHandler;
    private final TextMessageHandler textMessageHandler;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callbackHandler.handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> COMMAND_KEY.equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                commandHandler.handleCommandMessage(message, commandEntity.get());
                return;
            }
        }
        if (message.hasText()) textMessageHandler.handleTextMessage(message);
    }

}
