package com.atguigu.yygh.hosp;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Formattable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDemo {
    public static void main(String[] args) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//
//        System.out.println(format.format(new Date()));

    }

    public void list() {
        List<String> list = Arrays.asList("a", "b", "c", "d");

        List<String> collect = list.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collect); //[A, B, C, D]

        List<Integer> num = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> collect1 = num.stream().map(n -> n * 2).collect(Collectors.toList());
        System.out.println(collect1); //[2, 4, 6, 8, 10]
    }


}
