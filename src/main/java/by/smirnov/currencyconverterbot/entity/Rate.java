package by.smirnov.currencyconverterbot.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @JsonSetter("Cur_ID")
    @Column(name = "id")
    private Long id;

    @JsonSetter("Date")
    @Column(name = "date")
    private Timestamp date;

    @JsonSetter("Cur_Abbreviation")
    @Column(name = "abbreviation")
    private String abbreviation;

    @JsonSetter("Cur_Scale")
    @Column(name = "scale")
    private Long scale;

    @JsonSetter("Cur_Name")
    @Column(name = "name")
    private String name;

    @JsonSetter("Cur_OfficialRate")
    @Column(name = "official_rate")
    private Double officialRate;
}
