package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Currency;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NbrbCurrencyConversionService implements CurrencyConversionService {

    public static final String NBRB_RATES_URL = "https://www.nbrb.by/api/exrates/rates/";
    public static final String NBRB_KEY_RATE = "Cur_OfficialRate";
    public static final String NBRB_KEY_SCALE = "Cur_Scale";
    public static final String GET_METHOD = "GET";

    @Override
    public double getConversionRatio(Currency original, Currency target) {
        double originalRate = getRate(original);
        double targetRate = getRate(target);
        return originalRate / targetRate;
    }

    private double getRate(Currency currency) {
        if (currency == Currency.BYN) {
            return 1;
        }
        JSONObject json = getRateJson(getRateUrl(currency));
        double rate = json.getDouble(NBRB_KEY_RATE);
        double scale = json.getDouble(NBRB_KEY_SCALE);

        return rate / scale;
    }

    private URL getRateUrl(Currency currency) {
        try {
            return new URL(NBRB_RATES_URL + currency.getId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getRateJson(URL url) {
        StringBuilder response = new StringBuilder();

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET_METHOD);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(response.toString());
    }
}
