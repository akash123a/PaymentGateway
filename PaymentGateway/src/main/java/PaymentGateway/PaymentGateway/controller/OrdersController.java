package PaymentGateway.PaymentGateway.controller;


import java.util.Map;

import PaymentGateway.PaymentGateway.DTO.OrgRequestDTO;
import PaymentGateway.PaymentGateway.model.Orders;
import PaymentGateway.PaymentGateway.service.APIService;
import PaymentGateway.PaymentGateway.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.razorpay.RazorpayException;




@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:3000") // Allow from this origin

//@CrossOrigin(origins = "http://localhost:5174")

@RestController
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private APIService apiService;



    @GetMapping("/orders")
    public String ordersPage() {
        return "orders";
    }

    @GetMapping("/Check")
    public String Checkorder() {
        return "return Check";
    }

    @PostMapping(value = "/createOrder", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Orders> createOrder(@RequestBody Orders orders) throws RazorpayException{
        Orders razorpayOrder = orderService.createOrder(orders);
        return new ResponseEntity<>(razorpayOrder,HttpStatus.CREATED);
    }

    @PostMapping("/paymentCallback")
    public String paymentCallback(@RequestParam Map<String, String> response) {
        orderService.updateStatus(response);
        return "success";
    }

    // ✅ **Webhook Endpoint (Triggered by Razorpay)**
    @PostMapping("/webhook/razorpay")
    public ResponseEntity<String> razorpayWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Webhook Received: " + payload);

        // Handle payment update logic
        orderService.handleWebhook(payload);

        return ResponseEntity.ok("Webhook received successfully");
    }


    // ✅ **New API: Trigger Organisation Data Send**
    @PostMapping("/sendOrganisationData")
    public void sendOrganisationData(@RequestBody OrgRequestDTO orgRequestDTO) {
        //return apiService.sendData(orgRequestDTO);
    }
}