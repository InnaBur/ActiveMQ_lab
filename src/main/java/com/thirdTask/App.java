package com.thirdTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        logger.debug("Start");
//        Properties properties = new Properties();
        FileProcessing fileProcessing = new FileProcessing();


        MessageGenerator messageGenerator = new MessageGenerator();
        System.out.println("DATA" + messageGenerator.generateDataTime());
        System.out.println(messageGenerator.generateDataTime());
        System.out.println(messageGenerator.generateDataTime());





MyMessage message = new MyMessage();

message.setEddr("20231212-22222");
        System.out.println(message.getEddr());
for (int i = 0; i < 100; i++) {
    message.setName(messageGenerator.textGenerator());
    message.setCreated_at(messageGenerator.generateDataTime());
    message.setEddr(messageGenerator.eddrGenerator());
    message.setCount(messageGenerator.generateNumber());
    System.out.println(message.getName() + " " + message.getEddr() + " " + message.getCount()+ " " + message.getCreated_at());
}

        logger.debug("Finish");


//        @CreditCardNumber(ignoreNonDigitCharacters = true)

    }

    public String readOutputFormat() {
        String outputFormat = System.getProperty("N");
        if (outputFormat == null || Integer.parseInt(outputFormat) < 1000) {
            // delete before send !!!!!!!!
            outputFormat = "1001";
//            logger.warn("Output format must be more then 1000");
        }
        return outputFormat;
    }
}
