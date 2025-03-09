package PaymentGateway.PaymentGateway.repository;

import PaymentGateway.PaymentGateway.model.Orders;
import com.razorpay.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer>{

    Orders findByRazorpayOrderId(String razorpayId);

}