package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.components.commands.Commands;

public interface ActualCommandRepository {

    Commands getActualCommand(Long chatId);
    void setActualCommand(Long chatId, Commands command);
}
