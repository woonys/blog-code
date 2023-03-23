package ch3;

import org.junit.jupiter.api.Test;

public class LinkedListExample {

    @Test
    public void LinkedListExample() {
        //given
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);

        node1.next = node2;
        node2.next = node3;
        node3.next = null;
        //when

        System.out.println(node1);

        //then
    }
}
