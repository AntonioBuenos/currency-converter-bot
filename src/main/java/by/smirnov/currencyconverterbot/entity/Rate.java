package by.smirnov.currencyconverterbot.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Rate {

    @JsonSetter("Cur_ID")
    private Long id;
    @JsonSetter("Date")
    private Timestamp date;
    @JsonSetter("Cur_Abbreviation")
    private String abbreviation;
    @JsonSetter("Cur_Scale")
    private Long scale;
    @JsonSetter("Cur_Name")
    private String name;
    @JsonSetter("Cur_OfficialRate")
    private Double officialRate;
}
