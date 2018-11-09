package com.gedoumi.quwabao;

import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class QuwabaoTest {

    @Test
    public void test1() {
        System.out.println(new BigDecimal(1000.10000).setScale(5, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
    }

}
