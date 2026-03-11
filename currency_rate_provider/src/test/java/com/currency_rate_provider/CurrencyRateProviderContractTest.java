package com.currency_rate_provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Provider("CurrencyRateProviderService")
@PactBroker(host = "localhost", port = "9292")
public class CurrencyRateProviderContractTest {
    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 9090));
    }

    @Test
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void validatePacts(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
