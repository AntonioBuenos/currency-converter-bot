package by.smirnov.currencyconverterbot.service;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_HELP;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_SET_CURRENCY;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_START;
import static by.smirnov.currencyconverterbot.service.Constants.COMMAND_TODAY_RATES;

@UtilityClass
public class CommandListInit {

    private static final List<BotCommand> listofCommands;

    static {
        listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand(COMMAND_START, "get a welcome message"));
        listofCommands.add(new BotCommand(COMMAND_SET_CURRENCY, "calculate currencies exchange amounts"));
        listofCommands.add(new BotCommand(COMMAND_TODAY_RATES, "get today rates"));
        listofCommands.add(new BotCommand(COMMAND_HELP, "info how to use this bot"));
    }

    public static List<BotCommand> getCommands(){
        return listofCommands;
    }

}
