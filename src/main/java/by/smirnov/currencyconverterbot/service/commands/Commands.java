package by.smirnov.currencyconverterbot.service.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public enum Commands {

    SET_CURRENCY("/set_currency"),
    START("/start"),
    TODAY_RATES("/today_rates"),
    HELP("/help");

    private final String cmd;

    Commands(String cmd) {
        this.cmd = cmd;
    }

    public boolean equals(String cmd){
        return this.getCmd().equals(cmd);
    }
}
