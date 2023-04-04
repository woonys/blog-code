package ch3;

import java.util.Arrays;
import java.util.Collection;
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

        public Node() {
            this.data = null;
            this.next = null;
        }

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
        return this.size;
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
        Node node = head;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, node.data)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        E[] array = (E[]) toArray();
        return Arrays.asList(array).iterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        Node node = head;
        for (int i = 0; i < size; i++) {
            array[i] = node.data;
            node = node.next;
        }
        return array;
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
        Node prev = head;
        if (Objects.equals(o, prev.data)) {
            head = head.next;
            size--;
            return true;
        }
        for (int i = 0; i < size-1; i++) {
            if (Objects.equals(o, prev.next.data)) {
                prev.next = prev.next.next;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
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
        for (Object o : c) {
            remove(o);
        }
        return true;
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
        Node node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.data;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        E old = node.data;
        node.data = element;
        return old;

    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node addNode = new Node(element, null);
        if (index == 0) {
            addNode.next = head;
            head = addNode;
            size++;
            return;
        }
        Node prev = head;
        for (int i = 0; i < index-1; i++) {
            prev = prev.next;
        }
        Node next = prev.next;
        prev.next = addNode;
        addNode.next = next;
        size++;
    }

    @Override
    public E remove(int index) {
        int start = 0;
        E old;
        Node prev = head;
        if (Objects.equals(start, index)) {
             old = head.data;
            head = head.next;
            size--;
            return old;
        }
        for (int i = 0; i < index-1; i++) {
            prev = prev.next;
        }
        old = prev.next.data;
        prev.next = prev.next.next;
        return old;
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
        Node node = head;
        int lastIndex = -1;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, node.data)) {
                lastIndex = i;
            }
            node = node.next;
        }
        return lastIndex;
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
