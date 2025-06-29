package art.timestop.kafka.dispatch.util;

import java.util.UUID;

import art.timestop.kafka.dispatch.message.OrderCreated;

public class TestEventData {

    public static OrderCreated buildOrderCreatedEvent(UUID id, String item){
        return OrderCreated.builder()
            .orderId(id)
            .item(item)    
            .build();
    }

}
