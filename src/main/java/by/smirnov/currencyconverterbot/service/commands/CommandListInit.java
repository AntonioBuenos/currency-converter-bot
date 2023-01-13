package by.smirnov.currencyconverterbot.service.commands;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.ArrayList;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_HELP;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_SET_CURRENCY;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_START;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_TODAY_RATES;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_TOMORROW_RATES;

@UtilityClass
public class CommandListInit {

    private static final List<BotCommand> listofCommands;

    static {
        listofCommands = new ArrayList<>();
        for (Commands command : Commands.values()) {
            listofCommands.add(new BotCommand(command.getCmd(), command.getMessage()));
        }
    }

    public static SetMyCommands getCommands(){
        return new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null);
    }

}
