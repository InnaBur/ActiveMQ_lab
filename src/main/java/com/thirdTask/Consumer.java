package com.thirdTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import jakarta.validation.ConstraintViolation;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class Consumer extends ConnectionProcessing {
    //    CSVWriter writer = new CSVWriter(new FileWriter("valid.csv", true));
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    MyValidator validator = new MyValidator();

    public Consumer() throws IOException {
    }

    protected ArrayList<MyMessage> receiveMessage(ActiveMQConnectionFactory connectionFactory, Properties properties) throws JMSException, IOException {
        FileProcessing fileProcessing = new FileProcessing();

        Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination consumerDestination = consumerSession.createQueue(properties.getProperty("nameQueue"));

        // Create a message consumer from the session to the queue.
        final MessageConsumer consumer = consumerSession
                .createConsumer(consumerDestination);
        int count = 0;
        ArrayList<MyMessage> messagesList = new ArrayList<>();
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
//                validateMyMessage(myMessage);
//                fileProcessing.writeIntoFileAfterValidation("error.csv", dataValid(myMessage), validator.validateMessage(myMessage));


//                writeIntoFile(myMessage);
                messagesList.add(myMessage);
//                FileProcessing.writeDataLineByLine(writer, messagesList.get(count).getName(), messagesList.get(count).getCount()+"");
//                FileProcessing.writeDataLineByLine("valid.csv", name, eddr);

            } else {
                break;
            }
            count++;
        }

        logger.info("Messages received");
//        for (MyMessage ll: messagesList) {
//            System.out.println(ll);
//            FileProcessing.writeDataLineByLine(writer, ll.getName(), ll.getCount()+"");
//
//        }
        logger.info("received" + count);
        closeSession(consumer, consumerSession, consumerConnection);
        return messagesList;
    }

    private void validateMyMessage(MyMessage myMessage) throws IOException {
        String name = myMessage.getName();
        String countField = myMessage.getCount() + "";

        String[] messageArray = {name, countField};
        validator.validateMessage(myMessage);
    }

    public String[] dataValid(MyMessage myMessage) throws IOException {
        String name = myMessage.getName();
        String countField = myMessage.getCount() + "";

        return new String[]{name, countField};

    }

    public String[] dataInvalid(MyMessage myMessage, Set<ConstraintViolation<MyMessage>> validateMessage) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String name = myMessage.getName();
        String countField = myMessage.getCount() + "";
//        ArrayList<String> errorList = new ArrayList<>();
//        for (ConstraintViolation<MyMessage> m : validateMessage) {
//            String errorString = objectMapper.writeValueAsString(m);
//            errorList.add(errorString);
//        }

        List<String> err = validateMessage.stream().
                map(validate -> validate.getMessage())
                        .collect(Collectors.toList());

        String errors = objectMapper.writeValueAsString(err);



        return new String[]{name, countField, errors};

    }

    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }


}
