package by.epam.shpakova.factory;

import by.epam.shpakova.entity.Vehicle;

import java.util.Map;

public class QueueOfVehicle {

    public Vehicle[] creatorQueue(final Map<String, Object> parameters) {

        Vehicle[] queue = new Vehicle[(Integer) parameters.get("Queue")];

        for (int i = 0; i < queue.length; i++) {
            int vehicleSize
                    = (int) (Math.random()
                    * (Integer) parameters.get("MaxTerminalSize"))
                    + (Integer) parameters.get("MinTerminalSize");
            queue[i] = new Vehicle(vehicleSize, ("Vehicle number " + i));
        }
        return queue;

    }
}
