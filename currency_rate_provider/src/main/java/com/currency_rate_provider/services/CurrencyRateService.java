package com.currency_rate_provider.services;

import com.currency_rate_provider.web.dto.CurrencyResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@NoArgsConstructor
public class CurrencyRateService {
    public CurrencyResponse getCurrency() {
        Random rdm = new Random();
        return new CurrencyResponse()
                .withValue(rdm.nextDouble() * rdm.nextInt((1000 - 10) + 1) + 10)
                .withCalculationDateTime(LocalDateTime.now());
    }
}
