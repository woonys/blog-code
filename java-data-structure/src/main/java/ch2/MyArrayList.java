package ch2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MyArrayList<E> implements List<E> {
    int size;
    private E[] array;

    public MyArrayList() {
        array = (E[]) new Object[10];
        size = 0;
    }

    @Override
    public int size() {
        return size;
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
        for (E e : array) {
            if (Objects.equals(e, 0)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        // Array의 복사본을 만든다
        E[] copy = Arrays.copyOf(array, size);
        // 리스트로 변환한 뒤, 해당 리스트의 iterator() 메소드를 반환한다.
        return Arrays.asList(copy).iterator();
    }

    @Override
    public Object[] toArray() {
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E element) { // 리스트에 요소 추가하는 메소드
        // 만약 배열에 여분 공간이 없으면 더 큰 배열 만들어서 요소 위에 복사해야 함
        if (size >= array.length) {
            // 큰 배열 만들고 요소 복사
            E[] bigger = (E[]) new Object[array.length * 2]; // 2배로 늘린다 -> 꼭 2배가 정답은 아님. 파이썬은 루트 2배
            System.arraycopy(array, 0, bigger, 0, array.length);
            array = bigger;
        }
        array[size] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (Objects.equals(-1, i)) {
            return false;
        }
        remove(i);
        return true;
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
        boolean flag = false;
        for (Object obj : c) {
            flag |= remove(obj);
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        removeAll(List.of(array));
        size = 0;
    }

    @Override
    public E get(int index) { // 주어진 배열의 인덱스에 위치한 값을 반환
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public E set(int index, E element) {
        E old = get(index);
        array[index] = element;
        return old;
    }

    @Override
    public void add(int index, E element) { // 인덱스와 요소를 인자로 받는 메소드
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        add(element);
        for (int i = size - 1; i > index; i--) { // index 앞에는 건드리지 않고 그 뒤에만 다 한 칸씩 미룬다.
            array[i] = array[i - 1];
        }
        array[index] = element;
    }

    @Override
    public E remove(int index) {
        E element = get(index);
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i+1]; // 한 칸씩 땡긴다.
        }
        size--;
        return element;
    }

    @Override
    public int indexOf(Object o) {

        for (int i = 0; i< size; i++) {
            if (Objects.equals(array[i], o)) {
                return i;
            }
        }
        return -1;
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
