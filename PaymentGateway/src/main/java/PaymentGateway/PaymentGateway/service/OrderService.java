package PaymentGateway.PaymentGateway.service;

import java.util.Map;

import PaymentGateway.PaymentGateway.DTO.OrgRequestDTO;
import PaymentGateway.PaymentGateway.model.Orders;
import PaymentGateway.PaymentGateway.repository.OrdersRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

@Service
public class OrderService {

    @Autowired
    APIService apiService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Value("${razorpay.key.id}")
    private String razorpayId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(razorpayId, razorpaySecret);
    }

    // ✅ **Create Order on Razorpay**
    public Orders createOrder(Orders order) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("amount", order.getAmount() * 100); // amount in paise
        options.put("currency", "INR");
        options.put("receipt", order.getEmail());

        Order razorpayOrder = razorpayClient.orders.create(options);
        if (razorpayOrder != null) {
            order.setRazorpayOrderId(razorpayOrder.get("id"));
            order.setOrderStatus(razorpayOrder.get("status"));
        }
        return ordersRepository.save(order);
    }

    // ✅ **Update Payment Status (From Frontend Callback)**
    public Orders updateStatus(Map<String, String> map) {
        String razorpayId = map.get("razorpay_order_id");
        Orders order = ordersRepository.findByRazorpayOrderId(razorpayId);
        if (order != null) {
            order.setOrderStatus("PAYMENT DONE");
            return ordersRepository.save(order);
        }
        return null;
    }

    // ✅ **Handle Webhook Event from Razorpay**
    public void handleWebhook(Map<String, Object> payload) {
        try {
            String event = (String) payload.get("event");
            System.out.println("Webhook Received: " + event);

            // Extract `payload["payload"]["payment"]["entity"]` safely
            Map<String, Object> paymentObject = (Map<String, Object>) payload.get("payload");
            if (paymentObject == null || !paymentObject.containsKey("payment")) {
                System.out.println("Invalid Webhook Payload");
                return;
            }

            Map<String, Object> paymentEntity = (Map<String, Object>) ((Map<String, Object>) paymentObject.get("payment")).get("entity");
            if (paymentEntity == null) {
                System.out.println("Invalid Payment Entity");
                return;
            }

            if ("payment.captured".equals(event)) {
                String paymentId = (String) paymentEntity.get("id");
                String orderId = (String) paymentEntity.get("order_id");



                System.out.println("Payment Captured - Order ID: " + orderId + ", Payment ID: " + paymentId);

                // Update order status in the database
                Orders order = ordersRepository.findByRazorpayOrderId(orderId);
                OrgRequestDTO orgRequestDTO  = new OrgRequestDTO();
                    orgRequestDTO.setName(order.getName());
                    orgRequestDTO.setEmail(order.getEmail());
                         apiService.sendData(orgRequestDTO);
                if (order != null) {
                    order.setOrderStatus("PAID");
                    order.setPaymentId(paymentId);
                    ordersRepository.save(order);
                }
            }
        } catch (Exception e) {
            System.out.println("Error handling webhook: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
