package com.atguigu.yygh.hosp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formattable;

public class TestDemo {
    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(format.format(new Date()));

    }
}
