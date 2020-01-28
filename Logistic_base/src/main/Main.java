package main;

import by.epam.shpakova.entity.Base;
import by.epam.shpakova.entity.Product;
import by.epam.shpakova.entity.Vehicle;
import by.epam.shpakova.exception.DataReaderException;
import by.epam.shpakova.exception.ValidatorException;
import by.epam.shpakova.factory.QueueOfVehicle;
import by.epam.shpakova.parser.DataParser;
import by.epam.shpakova.reader.DataReader;
import by.epam.shpakova.thread.BaseTerritory;
import by.epam.shpakova.validator.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Main {

    final static String FILE_PATH = "E:\\HTP14\\Logistic_base\\data\\data.txt";
    final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        final Logger logger = LogManager.getLogger();
        DataReader dataReader = new DataReader();
        DataParser dataParser = new DataParser();
        List<Map<String, Object>> parametersList = new ArrayList<>();
        DataValidator dataValidator = new DataValidator();

        try {
            parametersList = dataParser.parseData(dataReader.readData(FILE_PATH));

        } catch (ValidatorException e) {
            logger.error("Data read from file is not valid.", e);
        } catch (DataReaderException e) {
            String message = "During the file reading an exception occurred.";
            logger.error("During the file reading an exception occurred.", e);

        }

        for (Map<String, Object> parameters : parametersList) {
            if (dataValidator.validatorData(parameters)) {
                run((Map<String, Object>) parameters.get("PortData"),
                        (Map<String, Object>) parameters.get("FleetData"));
            }
        }
    }

    public static void run(final Map<String, Object> baseData, Map<String, Object> queueData) throws InterruptedException {

        QueueOfVehicle queueOfVehicle = new QueueOfVehicle();

        Base.setParameters(baseData);
        Vehicle[] queue = queueOfVehicle.creatorQueue(queueData);

        for (Vehicle singleVehicle : queue) {
            for (int j = 0; j < singleVehicle.getTerminal().getCapacity(); j++) {
                singleVehicle.getTerminal().put(new Product("Product loading "));
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(Base.getAmountOfCars());
        List<Future<String>> list = new ArrayList<>();

        for (Vehicle vehicle : queue) {
            Callable<String> callable = new BaseTerritory(vehicle);
            Future<String> future = executor.submit(callable);
            list.add(future);
        }

        for (Future<String> future : list) {
            try {

              future.get();

            } catch (InterruptedException | ExecutionException e) {
              // logger.error("Exceptions in Main");
            }
        }
        executor.shutdown();
    }
    }
