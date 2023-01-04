package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.entity.Currencies;
import by.smirnov.currencyconverterbot.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_LIST_INIT_ERROR;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_SET_CURRENCY;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_START;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_TODAY_RATES;
import static by.smirnov.currencyconverterbot.service.Constants.CONVERSION_ERROR;
import static by.smirnov.currencyconverterbot.service.Constants.EDIT_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.service.Constants.ERROR;
import static by.smirnov.currencyconverterbot.service.Constants.FORMAT_RATES_RESPONSE;
import static by.smirnov.currencyconverterbot.service.Constants.MESSAGE_BAD_COMMAND;
import static by.smirnov.currencyconverterbot.service.Constants.MESSAGE_CHOOSE_CURRENCIES;
import static by.smirnov.currencyconverterbot.service.Constants.MESSAGE_START;
import static by.smirnov.currencyconverterbot.service.Constants.ORIGINAL;
import static by.smirnov.currencyconverterbot.service.Constants.TARGET;
import static by.smirnov.currencyconverterbot.service.TodayRateButtonsServiceImpl.TODAY_ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.service.TodayRateButtonsServiceImpl.TODAY_MAIN_CURRENCIES;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final CurrencyRepository currencyRepository;
    private final CurrencyConversionService currencyConversionService;
    private final BotConfig botConfig;
    private final MessageSender messageSender;
    private final TodayRateService todayRateService;
    private final TodayRateButtonsService todayRateButtonsService;
    public static final String DELIM = ":";

    public TelegramBot(CurrencyRepository currencyRepository,
                       CurrencyConversionService currencyConversionService,
                       BotConfig botConfig,
                       MessageSender messageSender,
                       TodayRateService todayRateService,
                       TodayRateButtonsService todayRateButtonsService) {
        this.currencyRepository = currencyRepository;
        this.currencyConversionService = currencyConversionService;
        this.botConfig = botConfig;
        this.messageSender = messageSender;
        this.todayRateService = todayRateService;
        this.todayRateButtonsService = todayRateButtonsService;
        initCommandsList();
    }

    private void initCommandsList() {
        try {
            this.execute(
                    new SetMyCommands(CommandListInit.getCommands(), new BotCommandScopeDefault(), null));
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
        long messageId = message.getMessageId();
        if (callbackData.equals(TODAY_MAIN_CURRENCIES)) {
            executeEditMessageText(todayRateService.getTodayMainRates(), chatId, messageId);
        } else if (callbackData.equals(TODAY_ALL_CURRENCIES)) {
            executeEditMessageText(todayRateService.getTodayRates(), chatId, messageId);
        } else processConversion(message, callbackQuery, chatId);

    }

    private void processConversion(Message message, CallbackQuery callbackQuery, long chatId) {
        String[] param = callbackQuery.getData().split(DELIM);
        String action = param[0];
        Currencies newCurrency = Currencies.valueOf(param[1]);
        switch (action) {
            case ORIGINAL -> currencyRepository.setOriginalCurrency(chatId, newCurrency);
            case TARGET -> currencyRepository.setTargetCurrency(chatId, newCurrency);
        }
        Currencies originalCurrency = currencyRepository.getOriginalCurrency(chatId);
        Currencies targetCurrency = currencyRepository.getTargetCurrency(chatId);
        List<List<InlineKeyboardButton>> buttons = getButtons(originalCurrency, targetCurrency);
        try {
            execute(
                    EditMessageReplyMarkup.builder()
                            .chatId(String.valueOf(chatId))
                            .messageId(message.getMessageId())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
        } catch (TelegramApiException e) {
            log.error(CONVERSION_ERROR, e.getMessage());
        }
    }

    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(EDIT_MESSAGE_ERROR, e.getMessage());
        }
    }

    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            handleCommandMessage(message);
            return;
        }
        if (message.hasText()) {
            handleTextMessage(message);
        }
    }

    private void handleCommandMessage(Message message) {
        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();

        if (commandEntity.isPresent()) {
            long chatId = message.getChatId();
            String command = this.getCommand(message, commandEntity);

            switch (command) {
                case COMMAND_START -> executeMessage(message, MESSAGE_START);
                case COMMAND_SET_CURRENCY -> {
                    Currencies originalCurrency =
                            currencyRepository.getOriginalCurrency(chatId);
                    Currencies targetCurrency = currencyRepository.getTargetCurrency(chatId);
                    var buttons = getButtons(originalCurrency, targetCurrency);
                    executeMessage(messageSender.sendMessage(message, MESSAGE_CHOOSE_CURRENCIES, buttons));
                }
                case COMMAND_TODAY_RATES -> executeMessage(todayRateButtonsService.getButtons(chatId));
                default -> executeMessage(message, MESSAGE_BAD_COMMAND);
            }
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR, e.getMessage());
        }
    }

    private List<List<InlineKeyboardButton>> getButtons(Currencies originalCurrency, Currencies targetCurrency) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Currencies currency : Currencies.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(originalCurrency, currency))
                                    .callbackData(ORIGINAL + DELIM + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData(TARGET + DELIM + currency)
                                    .build()));
        }
        return buttons;
    }

    private void handleTextMessage(Message message) {
        Optional<Double> value = parseDouble(message.getText());
        if (value.isPresent()) {
            Currencies originalCurrency = currencyRepository.getOriginalCurrency(message.getChatId());
            Currencies targetCurrency = currencyRepository.getTargetCurrency(message.getChatId());
            double ratio = currencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
            String rateMessage = String.format(FORMAT_RATES_RESPONSE,
                    value.get(), originalCurrency, (value.get() * ratio), targetCurrency);
            executeMessage(message, rateMessage);
        }
    }

    private Optional<Double> parseDouble(String messageText) {
        try {
            return Optional.of(Double.parseDouble(messageText));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String getCurrencyButton(Currencies saved, Currencies current) {
        return saved == current ? current + " ✅" : current.name();
    }

    private String getCommand(Message message, Optional<MessageEntity> commandEntity) {
        return message
                .getText()
                .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
    }

    private void executeMessage(Message message, String messageText) {
        try {
            execute(messageSender.sendMessage(message, messageText));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
