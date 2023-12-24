package by.smirnov.currencyconverterbot.service.rate

import by.smirnov.currencyconverterbot.service.spam.SpamService
import by.smirnov.currencyconverterbot.util.DateFormatter
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

import static by.smirnov.currencyconverterbot.constants.CommonConstants.TODAY
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TOMORROW
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_NEW_RATES
import static by.smirnov.currencyconverterbot.service.rate.DailyRateServiceImpl.NO_RATES_MESSAGE

@Service
@Slf4j
@TupleConstructor(includes = ['dailyRateService', 'spamService'], includeFields = true, includeProperties = false, force = true)
class RateScheduleService {

    @Autowired
    private final DailyRateService dailyRateService

    @Autowired
    private final SpamService spamService

    @Async
    @Scheduled(cron = '${cron.scheduler}')
    void deliverRateNews() {
        def date = getNextDate()
        def noRate = String.format(NO_RATES_MESSAGE, DateFormatter.format(date))
        String rateNews
        while (true){
            rateNews = dailyRateService.getMainRatesDynamic(date)
            if(rateNews != noRate) {
                spamService.spam(amendMessage(rateNews))
                return
            }
            sleepMinutes(5)
        }

    }

    private static LocalDate getNextDate(){
        def day = TODAY.getDayOfWeek()
        if(day == DayOfWeek.FRIDAY) return TOMORROW.plusDays(2)
        else if(day == DayOfWeek.SATURDAY) return TOMORROW.plusDays(1)
        else return TOMORROW
    }

    private static String getDayOfWeekName(){
        def date = getNextDate()
        date == TOMORROW ? "завтра" : "понедельник"
    }

    private static String amendMessage(String text){
        "${MESSAGE_NEW_RATES} ${getDayOfWeekName()}:\n\n${text}"
    }

    private static void sleepMinutes(Integer minutes) {
        try {
            Thread.sleep(Duration.ofMinutes(minutes).toMillis())
        } catch (InterruptedException e) {
            log.error(e.getMessage())
            Thread.currentThread().interrupt()
        }
    }
}
