package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.components.commands.Commands;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MapActualCommandRepository implements ActualCommandRepository{

    private final Map<Long, Commands> commands = new HashMap<>();

    @Override
    public Commands getActualCommand(Long chatId){
        return commands.get(chatId);
    }

    @Override
    public void setActualCommand(Long chatId, Commands command){
        commands.put(chatId, command);
    }
}
