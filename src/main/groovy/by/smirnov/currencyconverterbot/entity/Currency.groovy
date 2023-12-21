package by.smirnov.currencyconverterbot.entity

import com.fasterxml.jackson.annotation.JsonSetter
import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import java.sql.Timestamp

@Canonical
@Entity
@Table(name = "currency")
class Currency {

    @Id
    @JsonSetter("Cur_ID")
    @Column(name = "id")
    Long id

    @JsonSetter("Cur_ParentID")
    @Column(name = "parent_id")
    Long parentId

    @JsonSetter("Cur_Code")
    @Column(name = "code")
    String code

    @JsonSetter("Cur_Abbreviation")
    @Column(name = "abbreviation")
    String abbreviation

    @JsonSetter("Cur_Name")
    @Column(name = "name")
    String name

    @JsonSetter("Cur_Name_Bel")
    @Column(name = "name_eng")
    String nameEng

    @JsonSetter("Cur_Name_Eng")
    @Column(name = "name_bel")
    String nameBel

    @JsonSetter("Cur_QuotName")
    @Column(name = "quot_name")
    String quotName

    @JsonSetter("Cur_QuotName_Bel")
    @Column(name = "quot_name_eng")
    String quotNameEng

    @JsonSetter("Cur_QuotName_Eng")
    @Column(name = "quot_name_bel")
    String quotNameBel

    @JsonSetter("Cur_NameMulti")
    @Column(name = "name_multi")
    String nameMulti

    @JsonSetter("Cur_Name_BelMulti")
    @Column(name = "name_multi_eng")
    String nameMultiEng

    @JsonSetter("Cur_Name_EngMulti")
    @Column(name = "name_multi_bel")
    String nameMultiBel

    @JsonSetter("Cur_Scale")
    @Column(name = "scale")
    Long scale

    @JsonSetter("Cur_Periodicity")
    @Column(name = "periodicity")
    Integer periodicity

    @JsonSetter("Cur_DateStart")
    @Column(name = "date_start")
    Timestamp dateStart

    @JsonSetter("Cur_DateEnd")
    @Column(name = "date_end")
    Timestamp dateEnd
}
