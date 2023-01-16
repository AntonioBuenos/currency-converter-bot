package by.smirnov.currencyconverterbot.service.commands;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommandListInit {

    private static final List<BotCommand> listofCommands;

    static {
        listofCommands = new ArrayList<>();
        for (Commands command : Commands.values()) {
            if(command != Commands.SPAM) listofCommands.add(new BotCommand(command.getCmd(), command.getMessage()));
        }
    }

    public static SetMyCommands getCommands(){
        return new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null);
    }

}
