package com.thirdTask;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ConsumerTest {

    @Test
    void createConsumerAndReceiveMessages() {
    }

    @Test
    void ifReceiveMessages() throws JMSException, IOException, InterruptedException {

    }

    @Test
    void receiveOneMessage() {
    }

    @Test
    void ifCountTime() {
        LocalTime start = LocalTime.now().minusSeconds(30);
        int count = 300;
//        BlockingQueue<MyMessage> blockingQueuе = new LinkedBlockingQueue<>();
        assertEquals(10, new Consumer().countTime(start, count));
    }
}