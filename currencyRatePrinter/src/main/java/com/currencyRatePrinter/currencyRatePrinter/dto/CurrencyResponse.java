package com.currencyRatePrinter.currencyRatePrinter.dto;

import java.time.LocalDateTime;

public class CurrencyResponse {
    public Double value;
    public LocalDateTime calculationDatetime;

    @Override
    public String toString() {
        return "CurrencyResponse{" +
                "value=" + value +
                ", calculationDatetime=" + calculationDatetime +
                '}';
    }
}
