package by.smirnov.currencyconverterbot.service.client;

import by.smirnov.currencyconverterbot.entity.Rate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NbrbRateClientImpl implements NbrbRateClient{

    public static final String NBRB_TODAY_RATE_URL = "https://www.nbrb.by/api/exrates/rates?periodicity=0";

    @Override
    public List<Rate> getRates(){
        List<Rate> rates = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        try {
            rates = mapper.readValue(new URL(NBRB_TODAY_RATE_URL), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return rates;
    }
}
