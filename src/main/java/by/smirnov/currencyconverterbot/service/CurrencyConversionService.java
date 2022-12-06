package by.smirnov.currencyconverterbot.service;



public interface CurrencyConversionService {

    public interface CurrencyConversionService {

        static CurrencyConversionService getInstance() {
            return new NbrbCurrencyConversionService();
        }

        double getConversionRatio(Currency original, Currency target);
    }
}
