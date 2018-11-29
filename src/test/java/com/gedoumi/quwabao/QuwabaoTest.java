package com.gedoumi.quwabao;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class QuwabaoTest {

    @Test
    public void test1() throws ParseException {
        LocalDateTime now = LocalDateTime.now();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2018-10-05 12:04:25");
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Duration between = Duration.between(localDateTime, now);
        System.out.println("" + between.toNanos());
        System.out.println("Millis:" + between.toMillis());
        System.out.println("Minutes:" + between.toMinutes());
        System.out.println("Days:" + between.toDays());
    }

    @Test
    public void test2() {
        String mobile = "13810060370";
        String format = String.format("sms:%s", mobile);
        System.out.println(format);
    }

}
