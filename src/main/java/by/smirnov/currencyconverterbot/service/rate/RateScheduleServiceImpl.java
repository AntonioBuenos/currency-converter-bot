package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.service.spam.SpamService;
import by.smirnov.currencyconverterbot.util.DateFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.TODAY;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TOMORROW;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_NEW_RATES;
import static by.smirnov.currencyconverterbot.service.rate.DailyRateServiceImpl.NO_RATES_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateScheduleServiceImpl implements RateScheduleService {

    private final DailyRateService dailyRateService;
    private final SpamService spamService;

    @Override
    @Async
    @Scheduled(cron = "${cron.scheduler}")
    public void deliverRateNews() {
        LocalDate date = getNextDate();
        String noRate = String.format(NO_RATES_MESSAGE, DateFormatter.formatDate(date));
        String rateNews;
        while (true){
            rateNews = dailyRateService.getMainRatesDynamic(date);
            if(!rateNews.equals(noRate)) {
                spamService.spam(amendMessage(rateNews));
                return;
            }
            try {
                Thread.sleep(Duration.ofMinutes(5).toMillis());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

    }

    private LocalDate getNextDate(){
        DayOfWeek day = TODAY.getDayOfWeek();
        if(day.equals(DayOfWeek.FRIDAY)) return TOMORROW.plusDays(2);
        else if(day.equals(DayOfWeek.SATURDAY)) return TOMORROW.plusDays(1);
        else return TOMORROW;
    }

    private String getDayOfWeekName(){
        LocalDate date = getNextDate();
        if(date==TOMORROW) return "завтра";
        return "понедельник";
    }

    private String amendMessage(String text){
        return String.format("%s %s:\n\n%s", MESSAGE_NEW_RATES, getDayOfWeekName(), text);
    }
}
