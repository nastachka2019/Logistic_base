package by.epam.shpakova.thread;

import by.epam.shpakova.entity.Base;
import by.epam.shpakova.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class BaseTerritory  implements Callable<String> {      //куда заезжают машины

    private static final Logger logger = LogManager.getLogger();

    private final Vehicle vehicle;   //экземпляр машины, которая заехала и начинает погрузку/разгрузку


    public BaseTerritory(final Vehicle vehicleVal) {
        this.vehicle = vehicleVal;
    }

    @Override                              //погрузка/разгрузка
    public String call() throws Exception {
        Base base = Base.getInstance();
        while (!vehicle.getTerminal().isEmpty()) {

            base.getTerminal().put(vehicle.getTerminal().take());
            TimeUnit.MILLISECONDS.sleep(3);
        }


        while (!vehicle.getTerminal().isFull()) {

            vehicle.getTerminal().put(base.getTerminal().take());
            TimeUnit.MILLISECONDS.sleep(4);

        }

        logger.info(vehicle.getName() + " was served. ");

        return vehicle.getName();
    }
}
