package art.timestop.kafka.dispatch.service;

import static java.util.UUID.randomUUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.kafka.core.KafkaTemplate;

import art.timestop.kafka.dispatch.message.OrderCreated;
import art.timestop.kafka.dispatch.message.OrderDispatched;
import art.timestop.kafka.dispatch.util.TestEventData;

class DispatchServiceTest {

    private DispatchService service;
    private KafkaTemplate kafkaProducerMock;

    @BeforeEach
    void setUp() {
        kafkaProducerMock = mock(KafkaTemplate.class);
        service = new DispatchService(kafkaProducerMock);
    }

    @Test
    void process_Success() throws Exception {
        when(kafkaProducerMock.send(anyString(), any(OrderDispatched.class))).thenReturn(mock(CompletableFuture.class));

        OrderCreated testEvent = TestEventData.buildOrderCreatedEvent(randomUUID(), randomUUID().toString());
        service.process(testEvent);

        verify(kafkaProducerMock, times(1)).send(eq("order.dispatched"), any(OrderDispatched.class));
    }

    @Test
    public void process_ProducerThrowsException() {
        OrderCreated testEvent = TestEventData.buildOrderCreatedEvent(randomUUID(), randomUUID().toString());
        doThrow(new RuntimeException("Producer failure")).when(kafkaProducerMock).send(eq("order.dispatched"), any(OrderDispatched.class));

        Exception exception = assertThrows(RuntimeException.class, () -> service.process(testEvent));

        verify(kafkaProducerMock, times(1)).send(eq("order.dispatched"), any(OrderDispatched.class));
        assertThat(exception.getMessage(), equalTo("Producer failure"));
    }
}