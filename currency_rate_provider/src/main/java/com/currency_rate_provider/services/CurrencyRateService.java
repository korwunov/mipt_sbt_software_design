package com.currency_rate_provider.services;

import com.currency_rate_provider.web.dto.CurrencyResponse;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Log4j2
@Service
@NoArgsConstructor
public class CurrencyRateService {
    public CurrencyResponse getCurrency() {
        log.debug("Вызван метод getCurrency");
        Random rdm = new Random();
        double value = rdm.nextDouble() * rdm.nextInt((1000 - 10) + 1) + 10;

        // для тестирования метрики: если целая часть курса - четное число, тогда выбросим исключение и севрер ответит со статусом 500
        if ((int) value % 2 == 0) throw new RuntimeException();

        LocalDateTime dttm = LocalDateTime.now();
        log.debug("Рассчитано значение курса {} для даты-времени {}", value, dttm);
        return new CurrencyResponse()
                .withValue(value)
                .withCalculationDateTime(dttm);
    }
}
