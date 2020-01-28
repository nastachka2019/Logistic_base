package by.epam.shpakova.entity;

import by.epam.shpakova.thread.Terminal;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Base {

        private static Base instance;
     //Инструмент для контроля доступа к общему ресурсу несколькими потоками
        private static final Lock LOCK = new ReentrantLock();

    public static void setParameters(final Map<String, Object> parameters) { //пар-ры для инициал. экз. базы
            size = ((Integer) parameters.get("BaseTerminalSize"));
            amountOfCars= ((Integer) parameters.get("AmountOfCars"));
        }

        public static Base getInstance() {
            LOCK.lock();
            if (instance == null) {
                instance = new Base(size);
            }
            LOCK.unlock();
            return instance;
        }

        private static int size; //размеры терминала базы

        private Terminal terminal;  // кол-во машин, которое вмещает терминал
         private static int amountOfCars;

        public Base(final int terminalSize) {

            this.terminal = new Terminal(terminalSize);
        }

        public Terminal getTerminal() {
            return terminal;
        }

        public static int getAmountOfCars() {
            return amountOfCars;
        }

}
