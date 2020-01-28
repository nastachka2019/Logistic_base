package by.epam.shpakova.thread;

import by.epam.shpakova.entity.Product;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Terminal {        //кол-во товара,кот. хранится в терминале

    private int currentLoad;

    private final Product[] items;         //массив для хранения товаров
    private final Lock lock = new ReentrantLock();   //для контроля доступа к общему ресурсу несколькими потоками.

    private final Condition notFull = lock.newCondition();        //условие для одного потока, чтобы ждать,пока др.поток не уведомит о сост. загрузки

    private final Condition notEmpty = lock.newCondition();

    private int putIndex;  //какой товар прибыл

    private int takeIndex;  //какой товар был взят

    public Terminal (final int capacity) {
        this.items = new Product[capacity];
    }

    public boolean isEmpty() {         //true усли терминал пустой
        return currentLoad == 0;
    }

    public boolean isFull() {                    //true усли терминал заполнен
        return currentLoad == items.length;
    }

    public int getCapacity() {
        return items.length;
    }

    public void put(final Product product) throws InterruptedException {     //помещаем товар в терминал
        lock.lock();
        try {
            while (currentLoad == items.length) {
                notFull.await();
            }
            items[putIndex] = product;
            putIndex++;
            if (putIndex == items.length) {
                putIndex = 0;
            }
            currentLoad++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }

    }

    public Product take() throws InterruptedException {      //извлекаем товар из терминала
        lock.lock();
        try {
            while (currentLoad == 0) {
                notEmpty.await();
            }
            Product product = items[takeIndex];
            takeIndex++;
            if (takeIndex == items.length) {
                takeIndex = 0;
            }
            currentLoad--;
            notFull.signal();
            return product;
        } finally {
            lock.unlock();
        }

    }
}
