package com.example.javanullandemptystringexample;

public class ParseSpringClass {
    public class Strings {
        public String string;
    }
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(Strings.class).toPrintable());
    }
}
