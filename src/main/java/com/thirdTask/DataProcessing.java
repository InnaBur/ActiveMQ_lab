package com.thirdTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataProcessing {

    public String readOutputFormat() {
        String outputFormat = System.getProperty("N");
        if (outputFormat == null || Integer.parseInt(outputFormat) < 1000) {
            // delete before send !!!!!!!!
            outputFormat = "100000";
//            logger.error("Output format must be more than 1000");
            //System.exit(1);
        }
        return outputFormat;
    }

    public String[] dataValid(MyMessage myMessage) {
        String name = myMessage.getName();
        String countField = myMessage.getCount() + "";

        return new String[]{name, countField};

    }

    public String[] dataInvalid(MyMessage myMessage, Set<ConstraintViolation<MyMessage>> validateMessage) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String name = myMessage.getName();
        String countField = myMessage.getCount() + "";

        List<String> err = validateMessage.stream().
                map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        String errors = objectMapper.writeValueAsString(err);
        return new String[]{name, countField, errors};

    }
}
