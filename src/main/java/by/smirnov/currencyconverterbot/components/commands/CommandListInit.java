package by.smirnov.currencyconverterbot.components.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandListInit {

    private final List<BotCommand> menuCommands;
    private final SetMyCommands setMyCommands;

    {
        menuCommands = new ArrayList<>();
        for (Commands command : Commands.values()) {
            if(command != Commands.SPAM && command != Commands.UPD_CURRENCIES) {
                menuCommands.add(new BotCommand(command.getCmd(), command.getMessage()));
            }
        }
    }

    public CommandListInit() {
        this.setMyCommands = getCommands();
    }

    private SetMyCommands getCommands(){
        return new SetMyCommands(menuCommands, new BotCommandScopeDefault(), null);
    }

}
