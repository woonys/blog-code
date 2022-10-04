package org.example.dataformat.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateTimeFormatThreadSafeExample {

    // Singleton
    private static DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("dd-MMM-yyyy HH:mm:ss a")
        .toFormatter(Locale.ENGLISH);

    public static void main(String[] args) {
        String dateStr = "08-Aug-2022 12:58:47 PM";
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        Runnable task = new Runnable() {
            @Override
            public void run() {
                parseDate(dateStr);
            }
        };
        for(int i=0; i< 10; i++) {
            executorService.submit(task);
        }
        executorService.shutdown();
    }

    private static void parseDate(String dateStr) {
        try {
            LocalDateTime date = LocalDateTime.from(dateTimeFormatter.parse(dateStr));
            System.out.println("Successfully Parsed Date " + date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
