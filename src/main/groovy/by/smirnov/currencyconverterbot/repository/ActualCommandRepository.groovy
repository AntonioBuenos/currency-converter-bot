package by.smirnov.currencyconverterbot.repository

import by.smirnov.currencyconverterbot.components.commands.Commands

interface ActualCommandRepository {

    Commands getActualCommand(Long chatId)
    void setActualCommand(Long chatId, Commands command)
}