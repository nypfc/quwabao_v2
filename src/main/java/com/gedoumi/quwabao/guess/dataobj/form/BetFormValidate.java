package com.gedoumi.quwabao.guess.dataobj.form;

import com.gedoumi.quwabao.common.enums.GuessModeEnum;
import org.springframework.util.StringUtils;

/**
 * 下注接参验证
 *
 * @author Minced
 */
public final class BetFormValidate {

    private BetFormValidate() {
    }

    /**
     * 参数验证
     * 如果返回空正常；如果返回信息，说明验证失败
     *
     * @param bet         下注量
     * @param guessMode   下注玩法
     * @param guessNumber 下注号码
     * @return 通过返回空，不通过返回信息
     */
    public static String validate(Double bet, Integer guessMode, String guessNumber) {
        // 验证下注号码的合法性
        if (guessMode.equals(GuessModeEnum.MODE_1.getMode())) {
            if (guessNumber.length() != 1) return "玩法一的下注号码长度不正确";
            if (!guessNumberValidate(Integer.parseInt(guessNumber))) return "玩法一的下注号码不正确";
        } else if (guessMode.equals(GuessModeEnum.MODE_2.getMode())) {
            if (guessNumber.length() != 2) return "玩法二的下注号码长度不正确";
            int number1 = Integer.parseInt(String.valueOf(guessNumber.charAt(0)));
            int number2 = Integer.parseInt(String.valueOf(guessNumber.charAt(1)));
            if (number1 == number2) return "玩法二的两个号码不能相同";
            if (!(guessNumberValidate(number1) && guessNumberValidate(number2))) return "玩法二的下注号码不正确";
        } else if (guessMode.equals(GuessModeEnum.MODE_3.getMode())) {
            if (guessNumber.length() != 1) return "玩法三的下注号码长度不正确";
            int number = Integer.parseInt(guessNumber);
            if (number != 1 && number != 2) return "玩法三的下注号码不正确";
        } else {
            return "竞猜玩法不正确";
        }
        return null;
    }

    // 数字判断
    private static boolean guessNumberValidate(int number) {
        return number == 1 || number == 2 || number == 3 || number == 4 || number == 5 || number == 6;
    }

}
