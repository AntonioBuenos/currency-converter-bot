package by.smirnov.currencyconverterbot.components.buttons

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Message

interface ExchangeButtons {

    SendMessage getButtons (Message message)

    EditMessageReplyMarkup getButtons (Message message, long chatId)
}