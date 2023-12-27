package by.smirnov.currencyconverterbot.entity

import com.fasterxml.jackson.annotation.JsonSetter
import groovy.transform.Canonical
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

import java.time.LocalDate

@Canonical
@Entity
@Table(name = "rate")
class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id

    @JsonSetter("Cur_ID")
    @Column(name = "cur_id")
    Long curId

    @JsonSetter("Date")
    @Column(name = "date")
    LocalDate date

    @JsonSetter("Cur_Abbreviation")
    @Column(name = "abbreviation")
    String abbreviation

    @JsonSetter("Cur_Scale")
    @Column(name = "scale")
    Long scale

    @JsonSetter("Cur_Name")
    @Column(name = "name")
    String name

    @JsonSetter("Cur_OfficialRate")
    @Column(name = "official_rate")
    Double officialRate
}
