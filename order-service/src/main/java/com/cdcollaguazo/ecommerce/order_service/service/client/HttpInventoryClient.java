package com.cdcollaguazo.ecommerce.order_service.service.client;

import com.cdcollaguazo.ecommerce.order_service.dto.ReduceInventoryQuantityRequest;
import com.cdcollaguazo.ecommerce.order_service.exception.InvalidOrderException;
import com.cdcollaguazo.ecommerce.order_service.exception.UnavailableServiceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Component
public class HttpInventoryClient implements InventoryClient {

    private final WebClient webClient;
    private final Logger log = LoggerFactory.getLogger(HttpInventoryClient.class);

    public HttpInventoryClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    public void reduceInventoryQuantityRequest(String sku, ReduceInventoryQuantityRequest request) {
        webClient.patch()
                .uri("/api/v1/inventory/" + sku)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(ProblemDetail.class)
                                .map(problemDetail -> new InvalidOrderException(problemDetail.getDetail()))
                )
                .bodyToMono(Void.class)
                .block();
    }

    public CompletableFuture<Void> fallbackMethod(String sku, ReduceInventoryQuantityRequest request, Throwable throwable) {
        throw new UnavailableServiceException(throwable);
    }

}
