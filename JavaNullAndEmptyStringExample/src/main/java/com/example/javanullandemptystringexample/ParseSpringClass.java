package com.example.javanullandemptystringexample;

import org.openjdk.jol.info.ClassLayout;

public class ParseSpringClass {

    public static void main(String[] args) {
        String str = "";
        System.out.println(ClassLayout.parseInstance(str).toPrintable());
    }
}
