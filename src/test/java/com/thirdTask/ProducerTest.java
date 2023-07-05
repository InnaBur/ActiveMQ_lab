package com.thirdTask;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProducerTest {

    Session producerSession = Mockito.mock(Session.class);
    MessageProducer messageProducer = Mockito.mock(MessageProducer.class);
    ObjectMessage producerMessage = Mockito.mock(ObjectMessage.class);

    @Test
    void ifMessageSent() throws InterruptedException, JMSException {
        Producer producer = Mockito.mock(Producer.class);
        Properties properties = new Properties();
        MessageGenerator messageGenerator = Mockito.mock(MessageGenerator.class);
        properties.setProperty("poison", "10");
        LocalTime start = LocalTime.now();
        LocalTime estimatedEndTime = start.plusSeconds(Integer.parseInt(properties.getProperty("poison")));
        BlockingQueue<MyMessage> bl = new LinkedBlockingQueue<>();
        bl.put(new MyMessage());
        when(producer.isNotTimePoisonPill(estimatedEndTime)).thenReturn(true);

        producer.sendMessage(producerSession, messageProducer, messageGenerator, properties);
        verify(producer).sendMessage(producerSession, messageProducer, messageGenerator, properties);
    }

    @Test
    void ifPoisonPillSent() throws IOException, InterruptedException, JMSException {
        Producer producer = new Producer();

        MyMessage p = new MyMessage();
        BlockingQueue<MyMessage> bl = new LinkedBlockingQueue<>();
        bl.put(p);

        when(producerSession.createObjectMessage(p)).thenReturn(producerMessage);
        int count = producer.sendPoisonPill(1, producerSession, messageProducer);

        assertEquals(2, count);
    }

//    @Test
//    void isMessageToQueueSent() throws IOException, InterruptedException, JMSException {
//        Producer producer = new Producer();
//        MyMessage message = new MyMessage();
//        BlockingQueue<MyMessage> bl = new LinkedBlockingQueue<>();
//        bl.put(message);
//
//
//        when(producerSession.createObjectMessage(message)).thenReturn(producerMessage);
//        int messagesInQueue = 101;
//        int count = producer.sendMessageToQueue(messagesInQueue, bl, producerSession, messageProducer);
//
//        assertEquals(102, count);
//    }

    @Test
    void ifIsNotTimePoisonPill() throws IOException {
        Producer producer = new Producer();
        LocalTime end = LocalTime.now().plusSeconds(20);
         assertTrue(producer.isNotTimePoisonPill(end));
    }

}