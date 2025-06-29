package art.timestop.kafka.dispatch.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import art.timestop.kafka.dispatch.message.OrderCreated;
import art.timestop.kafka.dispatch.message.OrderDispatched;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";
    private final KafkaTemplate<String, Object> kafkaProducer;

    public void process(OrderCreated orderCreated) throws Exception{
        OrderDispatched orderDispatched = OrderDispatched.builder()
        .orderId(orderCreated.getOrderId())
        .build();

        kafkaProducer.send(ORDER_DISPATCHED_TOPIC, orderDispatched).get();
    }


}
