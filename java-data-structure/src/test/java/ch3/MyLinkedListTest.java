package ch3;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

import ch2.MyArrayListTest;

public class MyLinkedListTest extends MyArrayListTest {

    @BeforeEach
    public void setUp() {
        list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);

        mylist = new MyLinkedList<Integer>();
        mylist.addAll(list);
    }

}
