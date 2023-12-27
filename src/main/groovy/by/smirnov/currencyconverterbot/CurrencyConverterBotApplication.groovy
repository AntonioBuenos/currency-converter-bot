package by.smirnov.currencyconverterbot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = "by.smirnov.currencyconverterbot")
class CurrencyConverterBotApplication {

    static void main(String[] args) {
        SpringApplication.run(CurrencyConverterBotApplication, args)
    }
}
