package com.example.designpatterns.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 图书馆
 * @author w123x
 */
public class Library implements Iterable<Book>{

    private List<Book> books = new ArrayList<>();

    @Override
    public Iterator<Book> getIterator() {
        return new LibraryIterator();
    }

    public void addBook(Book book){
        books.add(book);
    }

    /**
     * 图书馆迭代器
     */
    private class LibraryIterator implements Iterator<Book>{
        private int index=0;

        @Override
        public boolean hasNext() {
            return index<books.size();
        }

        @Override
        public Book next() {
            Book book = books.get(index);
            index++;
            return book;
        }
    }
}
