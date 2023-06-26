package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import java.util.Properties;

public abstract class ConnectionProcessing {
    protected static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        return pooledConnectionFactory;
    }

    protected static ActiveMQConnectionFactory createActiveMQConnectionFactory(Properties properties) {
        String brokerAddress = properties.getProperty("brokerAddress");
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerAddress);

        connectionFactory.setUserName(properties.getProperty("username"));
        connectionFactory.setPassword(properties.getProperty("password"));
        return connectionFactory;
    }

}
