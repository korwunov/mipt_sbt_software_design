package com.currencyRatePrinter.currencyRatePrinter;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "CurrencyRateProviderService")
public class CurrencyRateProviderContractTest {
    @Pact(consumer = "CurrencyRatePrinterService")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .given("Currency received")
                .uponReceiving("A request to get currency")
                .path("/api/currency")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("{\"value\": 1, \"calculationDatetime\": \"2026-01-01T00:00:00\"}")
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void testCurrency(MockServer mockServer) {
        String response = new RestTemplate().getForObject(mockServer.getUrl() + "/api/currency", String.class);
        assertEquals("{\"value\": 1, \"calculationDatetime\": \"2026-01-01T00:00:00\"}", response);
    }
}
