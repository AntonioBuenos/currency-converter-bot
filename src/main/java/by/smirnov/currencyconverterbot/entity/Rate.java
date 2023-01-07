package by.smirnov.currencyconverterbot.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonSetter("Cur_ID")
    @Column(name = "cur_id")
    private Long curId;

    @JsonSetter("Date")
    @Column(name = "date")
    private LocalDate date;

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
