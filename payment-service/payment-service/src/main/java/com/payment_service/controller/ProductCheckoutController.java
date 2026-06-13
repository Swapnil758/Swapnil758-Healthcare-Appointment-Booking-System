package com.payment_service.controller;

import com.payment_service.client.BookingClient;
import com.payment_service.dto.BookingConfirmation;
import com.payment_service.dto.ProductRequest;
import com.payment_service.dto.StripeResponse;
import com.payment_service.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")

@RequestMapping("/product/v1")
public class ProductCheckoutController {


    private StripeService stripeService;
   private BookingClient bookingClient;

    public ProductCheckoutController(StripeService stripeService, BookingClient bookingClient) {
        this.stripeService = stripeService;
        this.bookingClient = bookingClient;
    }

//    public ProductCheckoutController(StripeService stripeService) {
//        this.stripeService = stripeService;
//    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(
       //     @RequestBody ProductRequest productRequest)
       @RequestParam long bookingId
       )
       {
           BookingConfirmation bookingDetails = bookingClient.getBookingById(bookingId);
           ProductRequest productRequest=new ProductRequest();
           productRequest.setName(bookingDetails.getClinicName());
           productRequest.setAmount(bookingDetails.getAmount());
           productRequest.setCurrency("inr");
           productRequest.setQuantity(1L);
           productRequest.setBookingId(bookingId);

        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponse);
    }
    @GetMapping("/success")
    public ResponseEntity<String> handleSuccess(
            @RequestParam("session_id") String sessionId
       //     @RequestParam("booking_id") String bookingId

    ) {
    private String stripeSecretKey = "REPLACE_WITH_YOUR_STRIPE_KEY";

        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();

            if ("paid".equalsIgnoreCase(paymentStatus)) {
                System.out.println("✅ Payment successful: true");
                bookingClient.updateBookingStatus(Long.parseLong(session.getMetadata().get("bookingId")));
                return ResponseEntity.ok("Payment successful");
            } else {
                System.out.println("❌ Payment not completed: false");
                return ResponseEntity.status(400).body("Payment not completed");
            }

        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Stripe error occurred");
        }
    }


    @GetMapping("/cancel")
    public ResponseEntity<String> handleCancel() {
        System.out.println("❌ Payment cancelled: false");
        return ResponseEntity.ok("Payment cancelled");
    }

}
