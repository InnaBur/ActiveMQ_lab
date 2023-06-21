package com.thirdTask;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MessageGenerator {
    FileProcessing fileProcessing = new FileProcessing();
    private final int NAME_MAX_LENGTH = Integer.parseInt(fileProcessing.readProperty("nameMaxLength"));
    private final int NAME_MIN_LENGTH = Integer.parseInt(fileProcessing.readProperty("nameMinLength"));
    private final int MAX_YEAR = Integer.parseInt(fileProcessing.readProperty("maxYear"));
    private final int MIN_YEAR = Integer.parseInt(fileProcessing.readProperty("minYear"));
    private final int EDDR_LENGTH = Integer.parseInt(fileProcessing.readProperty("eddrLength"));
    private final int A_LETTER = 97;
    private final int LAST_LETTER = 123;
    private final int MAX_MONTH = 12;
    private final int HOURS = 24;
    private final int MIN_SEC = 60;

    public MessageGenerator() throws IOException {
    }

    protected String textGenerator() {
        String text = "";
        int length = ThreadLocalRandom.current().nextInt(NAME_MIN_LENGTH, NAME_MAX_LENGTH);
        text = ThreadLocalRandom.current()
                .ints(length - 1, A_LETTER, LAST_LETTER)
                .mapToObj(codePoint -> String.valueOf((char) codePoint))
                .collect(Collectors.joining());

        text = Character.toUpperCase(text.charAt(0)) + text.substring(1);

        return text;
    }

    protected String eddrGenerator() {
        int lengthOfRandomDigits = 5;
        int monthBirth = Integer.parseInt(generateBirthData(MAX_MONTH + 1));
        return IntStream.range(0, EDDR_LENGTH - lengthOfRandomDigits)
                .mapToObj(i -> {
                    if (i == 0) {
                        return generateYear();
                    } else if (i == 1) {
                        return monthBirth + "";
                    } else if (i == 2) {
                        YearMonth yearMonth = YearMonth.of(MAX_YEAR, monthBirth);
                        int maxDayOfMonth = yearMonth.lengthOfMonth();
                        return generateBirthData(maxDayOfMonth);
                    } else {
                        return String.valueOf(ThreadLocalRandom.current().nextInt(0, 9));
                    }
                })
                .collect(Collectors.joining());
    }

    protected String generateDataTime() {
        int randomYear = ThreadLocalRandom.current().nextInt(MIN_YEAR, MAX_YEAR);
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
        int countMax = Integer.parseInt(new App().readOutputFormat());
        return ThreadLocalRandom.current().nextInt(countMax);
    }

    private String generateBirthData(int maxBorder) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, maxBorder);
        return String.format("%02d", randomNum);
    }

    private String generateYear() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(MIN_YEAR, MAX_YEAR));
    }
}
