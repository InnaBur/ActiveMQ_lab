package com.thirdTask;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MessageGenerator {
    FileProcessing fileProcessing = new FileProcessing();
    DataProcessing dataProcessing = new DataProcessing();


    private final int A_LETTER = 97;
    private final int LAST_LETTER = 123;
    private final int MAX_MONTH = 12;
    private final int HOURS = 24;
    private final int MIN_SEC = 60;
    Properties properties = fileProcessing.loadProperties();

    public MessageGenerator() throws IOException {
    }

    protected BlockingQueue<MyMessage> generateMessage() {
        int N = Integer.parseInt(dataProcessing.readOutputFormat());
        List<MyMessage> myMessages = Stream.generate(this::generateOneMessage)
                .limit(N)
                .collect(Collectors.toList());
        return new LinkedBlockingQueue<>(myMessages);
    }

    protected MyMessage generateOneMessage() {

        MyMessage message = new MyMessage();
        message.setName(textGenerator());
        message.setEddr(eddrGenerator());
        message.setCount(generateNumber());
        message.setCreated_at(generateDataTime());
        return message;
    }

    protected String textGenerator() {
        int nameMaxLength = Integer.parseInt(properties.getProperty("nameMaxLength"));
        int nameMinLength = Integer.parseInt(properties.getProperty("nameMinLength"));
        String text;
        int length = ThreadLocalRandom.current().nextInt(nameMinLength, nameMaxLength);
        text = ThreadLocalRandom.current()
                .ints(length - 1, A_LETTER, LAST_LETTER)
                .mapToObj(codePoint -> String.valueOf((char) codePoint))
                .collect(Collectors.joining());

        text = Character.toUpperCase(text.charAt(0)) + text.substring(1);

        return text;
    }

    protected String eddrGenerator() {
        int maxYear = Integer.parseInt(properties.getProperty("maxYear"));
        int minYear = Integer.parseInt(properties.getProperty("minYear"));
        int eddrLength = Integer.parseInt(properties.getProperty("eddrLength"));
        int lengthOfRandomDigits = 5;
        int monthBirth = Integer.parseInt(generateBirthData(MAX_MONTH + 1));

        return IntStream.range(0, eddrLength - lengthOfRandomDigits)
                .mapToObj(i -> {
                    if (i == 0) {
                        return generateYear(maxYear, minYear);
                    } else if (i == 1) {
                        return String.format("%02d", monthBirth);
                    } else if (i == 2) {
                        YearMonth yearMonth = YearMonth.of(maxYear, monthBirth);
                        int maxDayOfMonth = yearMonth.lengthOfMonth();
                        return generateBirthData(maxDayOfMonth);
                    } else {
                        return String.valueOf(ThreadLocalRandom.current().nextInt(1, 9));
                    }
                })
                .collect(Collectors.joining());
    }

    protected String generateDataTime() {
        int maxYear = Integer.parseInt(properties.getProperty("maxYear"));
        int minYear = Integer.parseInt(properties.getProperty("minYear"));
        int randomYear = ThreadLocalRandom.current().nextInt(minYear, maxYear);
        int randomMonth = ThreadLocalRandom.current().nextInt(1, MAX_MONTH + 1);
        YearMonth yearMonth = YearMonth.of(randomYear, randomMonth);
        int maxDayOfMonth = yearMonth.lengthOfMonth();
        int randomDate = ThreadLocalRandom.current().nextInt(1, maxDayOfMonth);
        int randomHours = ThreadLocalRandom.current().nextInt(HOURS);
        int randomMinutes = ThreadLocalRandom.current().nextInt(MIN_SEC);
        int randomSeconds = ThreadLocalRandom.current().nextInt(MIN_SEC);

        LocalDateTime date = LocalDateTime.of(randomYear, randomMonth, randomDate, randomHours, randomMinutes,
                randomSeconds);
        LocalDateTime dateTime = date.truncatedTo(ChronoUnit.SECONDS);
        return formatDate(dateTime);
    }

    protected static String formatDate(LocalDateTime date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
        return date.atOffset(ZoneOffset.UTC).format(dtf);
    }

    protected int generateNumber() {
        int countMax = Integer.parseInt(dataProcessing.readOutputFormat());
        return ThreadLocalRandom.current().nextInt(countMax);
    }

    private String generateBirthData(int maxBorder) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, maxBorder);
        return String.format("%02d", randomNum);
    }

    private String generateYear(int maxYear, int minYear) {
        return String.valueOf(ThreadLocalRandom.current().nextInt(minYear, maxYear));
    }
}
