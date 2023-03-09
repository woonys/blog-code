package ch2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class MyArrayListTest {
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

}