package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.Properties;

public class ConnectionProcessing {
    protected static ActiveMQConnectionFactory createActiveMQConnectionFactory(Properties properties) {
        String brokerAddress = properties.getProperty("brokerAddress");
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerAddress);

        connectionFactory.setUserName(properties.getProperty("username"));
        connectionFactory.setPassword(properties.getProperty("password"));
        return connectionFactory;
    }

}
