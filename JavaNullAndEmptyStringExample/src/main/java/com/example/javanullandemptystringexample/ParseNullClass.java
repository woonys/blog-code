package com.example.javanullandemptystringexample;

import org.openjdk.jol.info.ClassLayout;

public class ParseNullClass {

    public static void main(String[] args) {
        Object nullObj = null;
        System.out.println(ClassLayout.parseInstance(nullObj).toPrintable());
    }
}
