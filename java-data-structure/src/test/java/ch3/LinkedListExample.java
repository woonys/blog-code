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

        ListNode node0 = new ListNode(0, node1);
        //then
        ListNode node = node0;
        while (node != null) {
            System.out.println(node.data);
            node = node.next;
        }
    }
}
