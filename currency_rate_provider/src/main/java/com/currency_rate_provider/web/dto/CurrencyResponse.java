package com.currency_rate_provider.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CurrencyResponse {
    public Double value;
    public LocalDateTime calculationDatetime;

    public CurrencyResponse withValue(Double value) {
        this.value = value;
        return this;
    }

    public CurrencyResponse withCalculationDateTime(LocalDateTime dttm) {
        this.calculationDatetime = dttm;
        return this;
    }
}
