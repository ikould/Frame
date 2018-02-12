package com.adnonstop.frame.util;

import java.util.Random;

/**
 * 算法工具类
 * <p>
 * Created by ikould on 2017/10/9.
 */

public class MathUtil {

    /**
     * 概率算法 X 概率
     *
     * @param probability 概率信息
     * @return 序列
     */
    public static int getRandomInd(int[] probability) {
        Random ran = new Random();
        int count = 0;
        for (int i = 0; i < probability.length; i++) {
            count += probability[i];
        }
        if (count <= 0) {
            return -1;
        }
        int ran_num = ran.nextInt(count);
        int temp = 0;
        for (int i = 0; i < probability.length; i++) {
            temp += probability[i];
            if (ran_num <= temp)
                return i;
        }
        return -1;
    }

    /**
     * sin计算
     *
     * @param angle 角度
     * @return 计算结果
     */
    public static float sin(float angle) {
        return (float) Math.sin(angle * Math.PI / 180);
    }

    /**
     * cos计算
     *
     * @param angle 角度
     * @return 计算结果
     */
    public static float cos(float angle) {
        return (float) Math.cos(angle * Math.PI / 180);
    }
}
