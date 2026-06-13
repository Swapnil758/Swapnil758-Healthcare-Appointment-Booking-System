package com.payment_service.service;

import com.payment_service.dto.ProductRequest;
import com.payment_service.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    public StripeResponse checkoutProducts(ProductRequest productRequest) {

        Stripe.apiKey = secretKey;

        try {
            // Product Data
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productRequest.getName())
                            .build();

            // Price Data
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(productRequest.getCurrency() != null
                                    ? productRequest.getCurrency() : "inr")
                            .setUnitAmount((long) (productRequest.getAmount() * 100))
                            .setProductData(productData)
                            .build();

            // Line Item
            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(productRequest.getQuantity())
                            .setPriceData(priceData)
                            .build();

            // Session Params — :8086 sahi port!
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:8086/product/v1/success?session_id={CHECKOUT_SESSION_ID}")
                            .setCancelUrl("http://localhost:8086/product/v1/cancel")
                            .putMetadata("bookingId",
                                    String.valueOf(productRequest.getBookingId()))
                            .addLineItem(lineItem)
                            .build();

            // Create Session
            Session session = Session.create(params);

            // Build Response
            StripeResponse response = new StripeResponse();
            response.setStatus("SUCCESS");
            response.setMessage("Payment session created successfully");
            response.setSessionId(session.getId());
            response.setSessionUrl(session.getUrl());
            return response;

        } catch (StripeException e) {
            StripeResponse response = new StripeResponse();
            response.setStatus("FAILED");
            response.setMessage("Error: " + e.getMessage());
            response.setSessionId(null);
            response.setSessionUrl(null);
            return response;
        }
    }
}