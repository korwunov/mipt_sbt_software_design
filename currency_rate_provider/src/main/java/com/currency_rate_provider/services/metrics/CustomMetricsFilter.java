package com.currency_rate_provider.services.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomMetricsFilter extends OncePerRequestFilter {

    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, Counter> requestCountersByClient = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> error500CountersByClient = new ConcurrentHashMap<>();
    private final DistributionSummary responseTimeSummary;

    public CustomMetricsFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.responseTimeSummary = DistributionSummary.builder("http_response_time_seconds")
                .description("Response time distribution in seconds")
                .publishPercentileHistogram(true)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.nanoTime();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.nanoTime() - startTime;
            double durationInSeconds = duration / 1_000_000_000.0;

            String clientIdentifier = getClientIdentifier(request);

            Counter requestCounter = requestCountersByClient.computeIfAbsent(
                    clientIdentifier,
                    clientId -> Counter.builder("http_requests_total")
                            .description("Total HTTP requests")
                            .tag("client", clientId)
                            .register(meterRegistry)
            );
            requestCounter.increment();

            if (response.getStatus() >= 500) {
                Counter errorCounter = error500CountersByClient.computeIfAbsent(
                        clientIdentifier,
                        clientId -> Counter.builder("http_server_errors_total")
                                .description("Total 5xx server errors")
                                .tag("client", clientId)
                                .tag("error_code", "500")
                                .register(meterRegistry)
                );
                errorCounter.increment();
            }

            Timer.Sample sample = Timer.start(meterRegistry);
            sample.stop(Timer.builder("http_request_duration_seconds")
                    .tag("client", clientIdentifier)
                    .register(meterRegistry));

            DistributionSummary summary = DistributionSummary.builder("http_response_time_seconds")
                    .tag("client", clientIdentifier)
                    .publishPercentileHistogram(true)
                    .publishPercentiles(0.5, 0.95, 0.99)
                    .register(meterRegistry);
            summary.record(durationInSeconds);
        }
    }

    private String getClientIdentifier(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty()) {
            return realIp;
        }

        return request.getRemoteAddr();
    }
}
