package ch2;

import static ch2.SelectionSort.selectionSort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelectionSortTest {

    @Test
    public void SelectionSortTest() {
        //given
        int[] intArray = { 1, 5, 3, 2, 4 };
        int[] expectedArray = { 1, 2, 3, 4, 5 };
        //when
        selectionSort(intArray);

        //then
        Assertions.assertArrayEquals(expectedArray, intArray);
    }
}
