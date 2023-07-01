package com.thirdTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsumerTest {



    @Test
    void createConsumerAndReceiveMessages() {
    }

    @Test
    void ifReceiveMessages() throws JMSException, IOException, InterruptedException {

    }

    @Test
    void isMessageReceive() throws JMSException, InterruptedException {
        Consumer consumer = Mockito.mock(Consumer.class);
        MessageConsumer consumerMes = Mockito.mock(MessageConsumer.class);

        when(consumer.countMessages(consumerMes)).thenReturn(1);
        consumer.receiveMessages(consumerMes);

       verify(consumer).receiveMessages(consumerMes);
    }

//    @Test
//    void ifMessagesCounted() throws JMSException, IOException, InterruptedException {
//        Consumer consumer1 = Mockito.mock(Consumer.class);
//        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);
//        ObjectMessage objectMessage = Mockito.mock(ObjectMessage.class);
//        MyMessage myMessage = Mockito.mock(MyMessage.class);
//
//        when(consumer.receive(anyLong())).thenReturn(objectMessage);
//        when(objectMessage.getObject()).thenReturn(myMessage);
//        when(consumer1.isPoisonPill(myMessage)).thenReturn(true);
//
//        int count = consumer1.countMessages(consumer);
//
//        assertEquals(1, count);
//
//    }
    @Test
    void  testIsPoisonPill() {

       Consumer consumer = new Consumer();
        MyMessage message = new MyMessage();
        message.setName("PoisonPill");

        assertTrue(consumer.isPoisonPill(message));
    }

    @Test
    void ifCountTime() {

        LocalTime start = LocalTime.now().minusSeconds(30);
        int count = 300;
        double result = Math.round(new Consumer().countTime(start, count));
        assertEquals(10.0, result);
    }
}