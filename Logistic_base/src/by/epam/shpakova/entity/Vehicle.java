package by.epam.shpakova.entity;

import by.epam.shpakova.thread.Terminal;

public class Vehicle {

    private Terminal terminal;

    private String name;  // имя грузовика

    public Vehicle(final int terminalSize, final String nameVal) {
        terminal= new Terminal(terminalSize);     //сколько вмещает терминал
        this.name = nameVal;                           // имя грузовика
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public String getName() {
        return name;
    }
}
