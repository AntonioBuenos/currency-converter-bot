package by.smirnov.currencyconverterbot.controller;

import by.smirnov.currencyconverterbot.components.buttons.DailyRateButtons;
import by.smirnov.currencyconverterbot.components.buttons.ExchangeButtons;
import by.smirnov.currencyconverterbot.components.message.MessageSender;
import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository;
import by.smirnov.currencyconverterbot.repository.QueryDateRepository;
import by.smirnov.currencyconverterbot.service.conversion.CurrencyConversionService;
import by.smirnov.currencyconverterbot.service.currency.CurrencyService;
import by.smirnov.currencyconverterbot.service.rate.DailyRateService;
import by.smirnov.currencyconverterbot.service.user.UserService;
import by.smirnov.currencyconverterbot.util.DateParser;
import by.smirnov.currencyconverterbot.util.DoubleParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.Optional;

import static by.smirnov.currencyconverterbot.components.commands.Commands.HELP;
import static by.smirnov.currencyconverterbot.components.commands.Commands.RATES_BY_DATE;
import static by.smirnov.currencyconverterbot.components.commands.Commands.RATES_TODAY;
import static by.smirnov.currencyconverterbot.components.commands.Commands.RATES_TOMORROW;
import static by.smirnov.currencyconverterbot.components.commands.Commands.SET_CURRENCY;
import static by.smirnov.currencyconverterbot.components.commands.Commands.SPAM;
import static by.smirnov.currencyconverterbot.components.commands.Commands.START;
import static by.smirnov.currencyconverterbot.components.commands.Commands.UPD_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.Constants.ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_KEY;
import static by.smirnov.currencyconverterbot.constants.Constants.DELIM;
import static by.smirnov.currencyconverterbot.constants.Constants.FORMAT_RATES_RESPONSE;
import static by.smirnov.currencyconverterbot.constants.Constants.MAIN_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_BAD_COMMAND;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_INPUT_DATE;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_START;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_UNDER_CONSTRUCTION;
import static by.smirnov.currencyconverterbot.constants.Constants.ORIGINAL;
import static by.smirnov.currencyconverterbot.constants.Constants.TARGET;
import static by.smirnov.currencyconverterbot.constants.Constants.TODAY;
import static by.smirnov.currencyconverterbot.constants.Constants.TODAY_ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.Constants.TODAY_MAIN_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.Constants.TOMORROW;
import static by.smirnov.currencyconverterbot.constants.Constants.TOMORROW_ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.Constants.TOMORROW_MAIN_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.LogConstants.EDIT_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.LogConstants.EXECUTE_EDIT_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.LogConstants.EXECUTE_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.LogConstants.EXECUTE_SEND_MESSAGE_ERROR;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final MainCurrencyRepository mainCurrencyRepository;
    private final QueryDateRepository queryDateRepository;
    private final CurrencyConversionService currencyConversionService;
    private final BotConfig botConfig;
    private final MessageSender messageSender;
    private final DailyRateService dailyRateService;
    private final DailyRateButtons dailyRateButtons;
    private final ExchangeButtons exchangeButtons;
    private final CurrencyService currencyService;
    private final UserService userService;

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
            handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String callbackData = callbackQuery.getData();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        switch (callbackData) {
            case TODAY_MAIN_CURRENCIES, TOMORROW_MAIN_CURRENCIES, MAIN_CURRENCIES ->
                editMessage(dailyRateService.getMainRates(getDate(chatId)), chatId, messageId);
            case TODAY_ALL_CURRENCIES, TOMORROW_ALL_CURRENCIES, ALL_CURRENCIES ->
                editMessage(dailyRateService.getRates(getDate(chatId)), chatId, messageId);
            default -> processConversion(message, callbackData, chatId);
        }
    }

    private void processConversion(Message message, String callbackData, long chatId) {
        String[] param = callbackData.split(DELIM);
        String action = param[0];
        MainCurrencies newCurrency = MainCurrencies.valueOf(param[1]);
        if (action.equals(ORIGINAL)) mainCurrencyRepository.setOriginalCurrency(chatId, newCurrency);
        else if (action.equals(TARGET)) mainCurrencyRepository.setTargetCurrency(chatId, newCurrency);
        executeMessage(exchangeButtons.getButtons(message, chatId));
    }

    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> COMMAND_KEY.equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                handleCommandMessage(message, commandEntity.get());
                return;
            }
        }
        if (message.hasText()) handleTextMessage(message);
    }

    private void handleCommandMessage(Message message, MessageEntity commandEntity) {
        long chatId = message.getChatId();
        String command = message.getText().substring(commandEntity.getOffset(), commandEntity.getLength());

        if (START.equals(command)) {
            userService.registerUser(message);
            executeMessage(message, MESSAGE_START);
        }
        else if (HELP.equals(command)) executeMessage(message, MESSAGE_UNDER_CONSTRUCTION);
        else if (SET_CURRENCY.equals(command)) executeMessage(exchangeButtons.getButtons(message));
        else if (RATES_BY_DATE.equals(command)) executeMessage(message, MESSAGE_INPUT_DATE);
        else if (RATES_TODAY.equals(command)) executeMessage(dailyRateButtons.getButtons(chatId, TODAY));
        else if (RATES_TOMORROW.equals(command)) executeMessage(dailyRateButtons.getButtons(chatId, TOMORROW));
        else if (UPD_CURRENCIES.equals(command) && botConfig.getOwnerId() == chatId) {
            executeMessage(message, currencyService.saveAll());
        } else if (SPAM.equals(command) && botConfig.getOwnerId() == chatId) {
            executeMessage(message, MESSAGE_UNDER_CONSTRUCTION);
        } else executeMessage(message, MESSAGE_BAD_COMMAND);
    }

    private void handleTextMessage(Message message) {
        long chatId = message.getChatId();

        Double value = DoubleParser.parseDouble(message.getText());
        if (value != null) {
            MainCurrencies original = mainCurrencyRepository.getOriginalCurrency(chatId);
            MainCurrencies target = mainCurrencyRepository.getTargetCurrency(chatId);
            Double converted = currencyConversionService.convert(original, target, value);
            String rateMessage = String.format(FORMAT_RATES_RESPONSE, value, original, converted, target);
            executeMessage(message, rateMessage);
        } else {
            executeMessage(dailyRateButtons.getButtons(chatId, DateParser.parseDate(message.getText())));
        }
    }

    private void editMessage(String text, long chatId, int messageId) {
        try {
            execute(EditMessageText.builder()
                    .chatId(chatId)
                    .parseMode(ParseMode.HTML)
                    .text(text)
                    .messageId(messageId)
                    .build());
        } catch (TelegramApiException e) {
            log.error(EDIT_MESSAGE_ERROR, e.getMessage());
        }
    }

    private LocalDate getDate(Long chatId){
        return queryDateRepository.getDate(chatId);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(EXECUTE_SEND_MESSAGE_ERROR, e.getMessage());
        }
    }

    private void executeMessage(Message message, String messageText) {
        try {
            execute(messageSender.sendMessage(message, messageText));
        } catch (TelegramApiException e) {
            log.error(EXECUTE_MESSAGE_ERROR, e.getMessage());
        }
    }

    private void executeMessage(EditMessageReplyMarkup message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(EXECUTE_EDIT_MESSAGE_ERROR, e.getMessage());
        }
    }
}
