package com.example.designpatterns.iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 迭代器模式<br/>
 * 封装了容器的遍历，为不同的容器实现提供了统一的遍历方式
 * @author skyline
 */
public class IteratorMain {
    private static final Logger logger = LoggerFactory.getLogger(IteratorMain.class);
    public static void main(String[] args) {
        Library library = new Library();
        library.addBook(new Book("斯塔夫里阿诺斯","全球通史"));
        library.addBook(new Book("司马迁","史记"));
        library.addBook(new Book("孙武","孙子兵法"));
        library.addBook(new Book("克劳塞维茨","战争论"));
        library.addBook(new Book("比彻·斯托夫人","汤姆叔叔的小屋"));
        library.addBook(new Book("司汤达","红与黑"));
        library.addBook(new Book("雨果","悲惨世界"));
        library.addBook(new Book("罗贯中","三国演义"));

        Iterator<Book> iterator = library.getIterator();
        while (iterator.hasNext()){
            Book next = iterator.next();
            logger.info("找到了一本书{}", next);
        }
    }
}
