package by.smirnov.currencyconverterbot.repository

import by.smirnov.currencyconverterbot.components.commands.Commands
import org.springframework.stereotype.Component

@Component
class MapActualCommandRepository implements ActualCommandRepository{

    private final HashMap<Long, Commands> commands = [:]

    @Override
    Commands getActualCommand(Long chatId){
        commands.chatId
    }

    @Override
    void setActualCommand(Long chatId, Commands command){
        commands.chatId = command
    }
}