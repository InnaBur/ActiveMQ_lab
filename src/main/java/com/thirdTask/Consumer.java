package com.thirdTask;

import com.opencsv.CSVWriter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Consumer extends ConnectionProcessing {
    //    CSVWriter writer = new CSVWriter(new FileWriter("valid.csv", true));
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    MyValidator validator = new MyValidator();

    public Consumer() throws IOException {
    }

    protected void receiveMessage(ActiveMQConnectionFactory connectionFactory, Properties properties) throws JMSException, IOException {
        FileProcessing fileProcessing = new FileProcessing();

        Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination consumerDestination = consumerSession.createQueue(properties.getProperty("nameQueue"));

        // Create a message consumer from the session to the queue.
        final MessageConsumer consumer = consumerSession
                .createConsumer(consumerDestination);
        int count = 0;
        ArrayList<MyMessage> l = new ArrayList<>();
        int val = 0;
        int er = 0;
        // Begin to wait for messages.
        while (true) {
            final Message consumerMessage = consumer.receive(1000);
            if (consumerMessage instanceof ObjectMessage) {
//                MyMessage c = (MyMessage) (consumerMessage);
//                String name = c.getName();
//                int counte = c.getCount();
                System.out.println("MEs" + consumerMessage);
                // Get the POJO from the ObjectMessage.
                ObjectMessage consumerTextMessage = (ObjectMessage) consumerMessage;
                MyMessage myMessage = (MyMessage) consumerTextMessage.getObject();
                validator.validateMessage(myMessage);

//                writeIntoFile(myMessage);
                l.add(myMessage);
//                FileProcessing.writeDataLineByLine(writer, l.get(count).getName(), l.get(count).getCount()+"");
//                FileProcessing.writeDataLineByLine("valid.csv", name, eddr);

            } else {
                break;
            }
            count++;
        }

        logger.info("Messages received");
//        for (MyMessage ll: l) {
//            System.out.println(ll);
//            FileProcessing.writeDataLineByLine(writer, ll.getName(), ll.getCount()+"");
//
//        }
        logger.info("received" + count);
        closeSession(consumer, consumerSession, consumerConnection);

    }


    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }


}
