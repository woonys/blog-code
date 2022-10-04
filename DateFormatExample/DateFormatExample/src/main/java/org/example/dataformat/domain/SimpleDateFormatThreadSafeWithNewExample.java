package org.example.dataformat.domain;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleDateFormatThreadSafeWithNewExample {

    public static void main(String[] args) {
        String dateStr = "08-Aug-2022 12:58:47 PM";

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                parseDate(dateStr);
            }
        };

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }
        executorService.shutdown();
    }

    private static void parseDate(String dateStr) {
        try {
            // 매번 새 객체 생성 -> But 비용이 비싸다는 단점
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(dateStr);
            System.out.println("Successfully Parsed Date " + date);
        } catch (ParseException e) {
            System.out.println("ParseError " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
