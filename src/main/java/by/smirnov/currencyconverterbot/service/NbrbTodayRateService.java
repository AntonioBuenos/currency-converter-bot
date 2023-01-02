package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Rate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.StringJoiner;

@Service
@Slf4j
public class NbrbTodayRateService implements TodayRateService{

    public static final String NBRB_TODAY_RATE_URL = "https://www.nbrb.by/api/exrates/rates?periodicity=0";

    @Override
    public String getTodayRates(){
        ObjectMapper mapper = new ObjectMapper();
        List<Rate> rates = null;
        try {
            rates = mapper.readValue(new URL(NBRB_TODAY_RATE_URL), new TypeReference<>(){});
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return getRatesInfo(rates);
    }

    String getRatesInfo(List<Rate> rates){
        String header = String.format("Официальные курсы НБРБ на %tF:", rates.get(0).getDate());
        StringJoiner joiner = new StringJoiner("\n");
joiner.add(header);
        for (Rate rate : rates) {
            String rateInfo = String.format("%s (%d) = %f BYN", rate.getName(), rate.getScale(), rate.getOfficialRate());
            joiner.add(rateInfo);
        }
        return joiner.toString();
    }

}
