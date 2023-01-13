package by.smirnov.currencyconverterbot.controller;

import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository;
import by.smirnov.currencyconverterbot.service.buttons.ExchangeButtonsService;
import by.smirnov.currencyconverterbot.service.buttons.DailyRateButtonsService;
import by.smirnov.currencyconverterbot.service.commands.CommandListInit;
import by.smirnov.currencyconverterbot.service.conversion.CurrencyConversionService;
import by.smirnov.currencyconverterbot.service.message.MessageSender;
import by.smirnov.currencyconverterbot.service.rate.DailyRateService;
import by.smirnov.currencyconverterbot.util.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_KEY;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_LIST_INIT_ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.CONVERSION_ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.DELIM;
import static by.smirnov.currencyconverterbot.constants.Constants.EDIT_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.FORMAT_RATES_RESPONSE;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_BAD_COMMAND;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_START;
import static by.smirnov.currencyconverterbot.constants.Constants.ORIGINAL;
import static by.smirnov.currencyconverterbot.constants.Constants.TARGET;
import static by.smirnov.currencyconverterbot.constants.Constants.TODAY;
import static by.smirnov.currencyconverterbot.service.buttons.DailyRateButtonsServiceImpl.TODAY_ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.service.buttons.DailyRateButtonsServiceImpl.TODAY_MAIN_CURRENCIES;
import static by.smirnov.currencyconverterbot.service.commands.Commands.HELP;
import static by.smirnov.currencyconverterbot.service.commands.Commands.SET_CURRENCY;
import static by.smirnov.currencyconverterbot.service.commands.Commands.START;
import static by.smirnov.currencyconverterbot.service.commands.Commands.TODAY_RATES;
import static by.smirnov.currencyconverterbot.service.commands.Commands.TOMORROW_RATES;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final MainCurrencyRepository mainCurrencyRepository;
    private final CurrencyConversionService currencyConversionService;
    private final BotConfig botConfig;
    private final MessageSender messageSender;
    private final DailyRateService dailyRateService;
    private final DailyRateButtonsService dailyRateButtonsService;
    private final ExchangeButtonsService exchangeButtonsService;

    public TelegramBot(MainCurrencyRepository mainCurrencyRepository,
                       CurrencyConversionService currencyConversionService,
                       BotConfig botConfig,
                       MessageSender messageSender,
                       DailyRateService dailyRateService,
                       DailyRateButtonsService dailyRateButtonsService,
                       ExchangeButtonsService exchangeButtonsService) {
        this.mainCurrencyRepository = mainCurrencyRepository;
        this.currencyConversionService = currencyConversionService;
        this.botConfig = botConfig;
        this.messageSender = messageSender;
        this.dailyRateService = dailyRateService;
        this.dailyRateButtonsService = dailyRateButtonsService;
        this.exchangeButtonsService = exchangeButtonsService;
        initCommandsList();
    }

    private void initCommandsList() {
        try {
            this.execute(CommandListInit.getCommands());
        } catch (TelegramApiException e) {
            log.error(COMMAND_LIST_INIT_ERROR, e.getMessage());
        }
    }

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
        if (callbackData.equals(TODAY_MAIN_CURRENCIES)) {
            editMessage(dailyRateService.getMainRates(TODAY), chatId, messageId);
        } else if (callbackData.equals(TODAY_ALL_CURRENCIES)) {
            editMessage(dailyRateService.getRates(TODAY), chatId, messageId);
        } else processConversion(message, callbackData, chatId);
    }

    private void processConversion(Message message, String callbackData, long chatId) {
        String[] param = callbackData.split(DELIM);
        String action = param[0];
        MainCurrencies newCurrency = MainCurrencies.valueOf(param[1]);
        if (action.equals(ORIGINAL)) mainCurrencyRepository.setOriginalCurrency(chatId, newCurrency);
        else if (action.equals(TARGET)) mainCurrencyRepository.setTargetCurrency(chatId, newCurrency);
        executeMessage(exchangeButtonsService.getButtons(message, chatId));
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

        if (START.equals(command)) executeMessage(message, MESSAGE_START);
        else if (SET_CURRENCY.equals(command)) executeMessage(exchangeButtonsService.getButtons(message));
        else if (TODAY_RATES.equals(command)) executeMessage(dailyRateButtonsService.getButtons(chatId, TODAY));
        else if (TOMORROW_RATES.equals(command)) executeMessage(message, "Данный функционал появится в ближайшее время");
        else if (HELP.equals(command)) executeMessage(message, "Данный функционал появится в ближайшее время");
        else executeMessage(message, MESSAGE_BAD_COMMAND);
    }

    private void handleTextMessage(Message message) {
        Double value = Parser.parseDouble(message.getText());
        if (value != null) {
            long chatId = message.getChatId();
            MainCurrencies original = mainCurrencyRepository.getOriginalCurrency(chatId);
            MainCurrencies target = mainCurrencyRepository.getTargetCurrency(chatId);
            Double converted = currencyConversionService.convert(original, target, value);
            String rateMessage = String.format(FORMAT_RATES_RESPONSE, value, original, converted, target);
            executeMessage(message, rateMessage);
        }
    }

    private void editMessage(String text, long chatId, int messageId) {
        try {
            execute(EditMessageText.builder()
                    .chatId(chatId)
                    .text(text)
                    .messageId(messageId)
                    .build());
        } catch (TelegramApiException e) {
            log.error(EDIT_MESSAGE_ERROR, e.getMessage());
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR, e.getMessage());
        }
    }

    private void executeMessage(Message message, String messageText) {
        try {
            execute(messageSender.sendMessage(message, messageText));
        } catch (TelegramApiException e) {
            log.error(ERROR, e.getMessage());
        }
    }

    private void executeMessage(EditMessageReplyMarkup message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(CONVERSION_ERROR, e.getMessage());
        }
    }
}
