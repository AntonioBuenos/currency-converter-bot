package by.smirnov.currencyconverterbot.components.commands;

import by.smirnov.currencyconverterbot.controller.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.LogConstants.COMMAND_LIST_INIT_ERROR;

@Component
@Slf4j
public class CommandListInit {

    private final TelegramBot bot;

    public CommandListInit(TelegramBot bot) {
        this.bot = bot;
        initCommandMenu();
    }

    private void initCommandMenu() {
        try {
            bot.execute(new SetMyCommands(getCommandsMenu(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(COMMAND_LIST_INIT_ERROR, e.getMessage());
        }
    }

    private List<BotCommand> getCommandsMenu() {
        return Arrays.stream(Commands.values())
                .filter(command -> command != Commands.SPAM && command != Commands.UPD_CURRENCIES)
                .map(command -> new BotCommand(command.getCmd(), command.getMessage()))
                .toList();
    }
}
