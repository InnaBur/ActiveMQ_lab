package com.thirdTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataProcessing {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessing.class);
    public String readOutputFormat() {
        String outputFormat = System.getProperty("N");
        if (outputFormat == null || Integer.parseInt(outputFormat) < 1000) {
            outputFormat="100000";
//            logger.error("Output format must be more than 1000");
//            System.exit(1);
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
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        String name = myMessage.getName();
        String countField = myMessage.getCount() + "";

        List<String> err = validateMessage.stream().
                map(ConstraintViolation::getMessage).
                map(message -> message.replace("\"\"", ""))
                .collect(Collectors.toList());

        Map<String, List<String>> jsonMap = new HashMap<>();

        jsonMap.put("errors", err);

        String errors = objectMapper.writeValueAsString(jsonMap);

        return new String[]{name, countField, errors};
    }
}
