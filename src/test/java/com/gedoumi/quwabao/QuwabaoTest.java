package com.gedoumi.quwabao;

import com.gedoumi.quwabao.common.utils.IDGeneratorUtil;
import com.gedoumi.quwabao.common.utils.PasswordUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        IDGeneratorUtil IDGeneratorUtil = new IDGeneratorUtil();
        for (int i = 0; i < 100; i++) {
            long id = IDGeneratorUtil.nextId();
            System.out.println(id);
        }
    }

    @Test
    public void test3() {
        Date date1 = new Date(1544427006000L);
        Date date2 = new Date(1544198400000L);
        Date date3 = new Date(1541347200000L);
        ArrayList<Date> dates = Lists.newArrayList(date1, date2, date3);
        List<Date> collect = dates.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        collect.forEach(value -> System.out.println(sdf.format(value)));
    }

    @Test
    public void test4() {
        String s = PasswordUtil.payPasswordEncrypt(312L, "111111");
        System.out.println(s);
    }

}
