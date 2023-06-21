package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import java.io.IOException;
import java.util.Properties;

public abstract class ConnectionProcessing {
    protected static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        // Create a pooled connection factory.
        final PooledConnectionFactory pooledConnectionFactory =
                new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
//        pooledConnectionFactory.setMaxConnections(10);
        return pooledConnectionFactory;
    }

    protected static ActiveMQConnectionFactory createActiveMQConnectionFactory(Properties properties) throws IOException {
        FileProcessing fileProcessing = new FileProcessing();
        String brokerAddress = properties.getProperty("brokerAddress");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        // Create a connection factory.
        final ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(brokerAddress);

        // Pass the sign-in credentials.
        connectionFactory.setUserName(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }
}
