package com.cdcollaguazo.ecommerce.order_service.service.client;

import com.cdcollaguazo.ecommerce.order_service.dto.ReduceInventoryQuantityRequest;
import com.cdcollaguazo.ecommerce.order_service.exception.InvalidOrderException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HttpInventoryClient implements InventoryClient {

    private final WebClient webClient;

    public HttpInventoryClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
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

}
