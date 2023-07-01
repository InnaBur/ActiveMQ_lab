package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionProcessingTest {

    @Test
    void ifActiveMQConnectionFactoryCreated() {
        Properties properties = new Properties();
        properties.setProperty("brokerAddress", "tcp://localhost:61616");
        properties.setProperty("username", "Inna");
        properties.setProperty("password", "12345");

        ActiveMQConnectionFactory connectionFactory = ConnectionProcessing.createActiveMQConnectionFactory(properties);

        assertEquals("tcp://localhost:61616", connectionFactory.getBrokerURL());
        assertEquals("Inna", connectionFactory.getUserName());
        assertEquals("12345", connectionFactory.getPassword());
    }
}