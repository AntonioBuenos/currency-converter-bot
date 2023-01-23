package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.components.commands.Commands;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
