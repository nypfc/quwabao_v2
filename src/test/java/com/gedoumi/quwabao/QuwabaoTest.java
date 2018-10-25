package com.gedoumi.quwabao;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QuwabaoTest {

    @Test
    public void bigDecimalTest() {
        System.out.println(new BigDecimal(1000.10000).setScale(5, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
    }

    @Test
    public void dateTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1Str = "2018-10-20 16:09:00";
        String date2Str = "2018-10-20 16:10:00";
        Date parse1 = sdf.parse(date1Str);
        Date parse2 = sdf.parse(date2Str);
        long diff = (parse2.getTime() / 1000) - (parse1.getTime() / 1000);
        System.out.println(diff);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(parse1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(parse2);
        int i = calendar2.get(Calendar.MINUTE) - calendar1.get(Calendar.MINUTE);
        System.out.println(i);
    }

}
