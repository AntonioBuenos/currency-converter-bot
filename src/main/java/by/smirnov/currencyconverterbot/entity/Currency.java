package by.smirnov.currencyconverterbot.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @JsonSetter("Cur_ID")
    @Column(name = "id")
    private Long id;
    @JsonSetter("Cur_ParentID")
    @Column(name = "code")
    private String code;
    @JsonSetter("Cur_Code")
    @Column(name = "abbreviation")
    private String abbreviation;
    @JsonSetter("Cur_Abbreviation")
    @Column(name = "name")
    private String name;
    @JsonSetter("Cur_Name")
    @Column(name = "name_eng")
    private String nameEng;
    @JsonSetter("Cur_Name_Bel")
    @Column(name = "name_bel")
    private String nameBel;
    @JsonSetter("Cur_Name_Eng")
    @Column(name = "quot_name")
    private String quotName;
    @JsonSetter("Cur_QuotName")
    @Column(name = "quot_name_eng")
    private String quotNameEng;
    @JsonSetter("Cur_QuotName_Bel")
    @Column(name = "quot_name_bel")
    private String quotNameBel;
    @JsonSetter("Cur_QuotName_Eng")
    @Column(name = "name_multi")
    private String nameMulti;
    @JsonSetter("Cur_NameMulti")
    @Column(name = "name_multi_eng")
    private String nameMultiEng;
    @JsonSetter("Cur_Name_BelMulti")
    @Column(name = "name_multi_bel")
    private String nameMultiBel;
    @JsonSetter("Cur_Name_EngMulti")
    @Column(name = "scale")
    private Long scale;
    @JsonSetter("Cur_Scale")
    @Column(name = "periodicity")
    private Integer periodicity;
    @JsonSetter("Cur_Periodicity")
    @Column(name = "date_start")
    private Date dateStart;
    @JsonSetter("Cur_DateStart")
    @Column(name = "date_end")
    private Date dateEnd;

}
