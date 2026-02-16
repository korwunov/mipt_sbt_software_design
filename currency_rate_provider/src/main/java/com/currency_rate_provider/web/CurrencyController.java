package com.currency_rate_provider.web;

import com.currency_rate_provider.web.dto.CurrencyResponse;
import com.currency_rate_provider.services.CurrencyRateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    @Autowired
    private CurrencyRateService currencyRateService;

    @GetMapping
    public CurrencyResponse getCurrency() {
        log.info("Получен GET запрос на получение курса");
        return currencyRateService.getCurrency();
    }
}
