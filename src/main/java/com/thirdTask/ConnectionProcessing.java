package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import java.util.Properties;

public class ConnectionProcessing {
    protected static ActiveMQConnectionFactory createActiveMQConnectionFactory(Properties properties) {
        String brokerAddress = properties.getProperty("brokerAddress");
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerAddress);

        connectionFactory.setUserName(properties.getProperty("username"));
        connectionFactory.setPassword(properties.getProperty("password"));
        return connectionFactory;
    }
    protected static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        // Create a pooled connection factory.
        final PooledConnectionFactory pooledConnectionFactory =
                new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
        return pooledConnectionFactory;
    }
}
