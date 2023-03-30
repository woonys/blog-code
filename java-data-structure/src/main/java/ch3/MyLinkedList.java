package ch3;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MyLinkedList<E> implements List<E> {
    private int size;
    private Node head;

    private class Node {
        public E data;
        public Node next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }
        public Node(E data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    public MyLinkedList() {
        head = null;
        size = 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        if (Objects.equals(size, 0)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        if (head == null) {
            head = new Node(e);
        } else {
            Node node = head;
            for (; node.next != null; node = node.next) {}
            node.next = new Node(e);
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = true;
        for (E element: c) {
            flag &= add(element);
        }
        return flag;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        Node node = head;
        for (int i=0; i<size; i++) {
            if (Objects.equals(o, node.data)) {
                return i;
            }
            node = node.next;
        }
        return -1;
        /* My solution
//        if (head == null) {
//            return -1;
//        } else if (Objects.equals(head.data, o)) {
//            return 0;
//        } else {
//            int index = 0;
//            Node node = head;
//            for (; node.next != null; node = node.next) {
//                index++;
//                if (Objects.equals(node.data, o)) {
//                    return index;
//                }
//            }
//        }
//        return -1;
         */
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }


}
