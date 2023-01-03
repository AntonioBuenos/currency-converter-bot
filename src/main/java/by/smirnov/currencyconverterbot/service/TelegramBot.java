package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.entity.Currencies;
import by.smirnov.currencyconverterbot.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
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

import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_SET_CURRENCY;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_START;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_TODAY_RATES;
import static by.smirnov.currencyconverterbot.service.Constants.FORMAT_RATES_RESPONSE;
import static by.smirnov.currencyconverterbot.service.Constants.MESSAGE_CHOOSE_CURRENCIES;
import static by.smirnov.currencyconverterbot.service.Constants.ORIGINAL;
import static by.smirnov.currencyconverterbot.service.Constants.TARGET;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final CurrencyRepository currencyRepository;
    private final CurrencyConversionService currencyConversionService;
    private final BotConfig botConfig;
    private final MessageSender messageSender;
    private final TodayRateService todayRateService;
    private final CurrencyService currencyService;
    public static final String DELIM = ":";

    public TelegramBot(CurrencyRepository currencyRepository,
                       CurrencyConversionService currencyConversionService,
                       BotConfig botConfig,
                       MessageSender messageSender,
                       TodayRateService todayRateService, CurrencyService currencyService) {
        this.currencyRepository = currencyRepository;
        this.currencyConversionService = currencyConversionService;
        this.botConfig = botConfig;
        this.messageSender = messageSender;
        this.todayRateService = todayRateService;
        this.currencyService = currencyService;
        try {
            this.execute(new SetMyCommands(CommandListInit.getCommands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
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
        Long chatId = message.getChatId();
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
                            .chatId(chatId.toString())
                            .messageId(message.getMessageId())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
            String command = this.getCommand(message, commandEntity);
            switch (command) {
                case COMMAND_START -> executeMessage(message,
                        "Вас приветствует бот курсов валют! Используйте меню команд.");
                case COMMAND_SET_CURRENCY -> {
                    Currencies originalCurrency =
                            currencyRepository.getOriginalCurrency(message.getChatId());
                    Currencies targetCurrency = currencyRepository.getTargetCurrency(message.getChatId());
                    try {
                        execute(messageSender.sendMessage(
                                message,
                                MESSAGE_CHOOSE_CURRENCIES,
                                getButtons(originalCurrency, targetCurrency)));
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                }
                case COMMAND_TODAY_RATES -> executeMessage(message, todayRateService.getTodayRates());
                default -> executeMessage(message, "Command not recognized!");
            }
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
