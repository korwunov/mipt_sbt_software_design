package com.currencyRatePrinter.currencyRatePrinter.service;

import com.currencyRatePrinter.currencyRatePrinter.dto.CurrencyResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
@Log4j2
public class RatePrinterService {
    private static final String ROUTE = "/api/currency";

    @Value("${spring.application.currencyRateProviderHost}")
    private String host;

    private final RestTemplate restTemplate;

    public RatePrinterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRateString = "${spring.application.clientRequestInterval: #{5000}}")
    public void getCurrencyRate() {
        try {
            CurrencyResponse response = restTemplate.getForObject(host + ROUTE, CurrencyResponse.class);
            log.info("Получен ответ от currencyProvider {}", response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Ошибка получения курса {}", e.getMessage());
        }
    }
}
