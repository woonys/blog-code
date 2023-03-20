package ch2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyArrayListTest {
    protected List<Integer> mylist;
    protected List<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);

        mylist = new MyArrayList<Integer>();
        mylist.addAll(list);
    }

    @Test
    public void MyArrayListTest() throws Exception {
        //given
        List<String> arr = new MyArrayList();
        arr.add("Hello");
        arr.add("This");
        arr.add("is");
        arr.add("Jungle");

        //when
        arr.set(0, "Bye");
        //then
        assertEquals("Bye", arr.get(0));
    }

    @Test
    public void testMyList() {
        assertEquals(mylist.size(), 3);
    }

    @Test
    public void testAddIntT() {
        mylist.add(1, 5);
        assertEquals(mylist.get(1), Integer.valueOf(5));
        assertEquals(mylist.size(), 4);

        try {
            mylist.set(-1, 0);
            fail();
        } catch (IndexOutOfBoundsException e) {

        }
        mylist.add(0, 6);
        assertEquals(mylist.get(0), 6);

        mylist.add(5, 7);
        assertEquals(mylist.get(5), Integer.valueOf(7));
    }

    @Test
    public void testIndexOfNull() {
        assertEquals(mylist.indexOf(null), -1);
        mylist.add(null);
        assertEquals(mylist.indexOf(null), 3);
    }


}