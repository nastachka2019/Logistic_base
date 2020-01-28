package by.epam.shpakova.validator;

import java.util.Map;

public class DataValidator {
    public boolean validatorData(final Map<String, Object> expParams) {

        if (!validatorBase(expParams.get("BaseData"))) {
            return false;
        }
        return validatorQueue(expParams.get("QueueData"));
    }

    public boolean validatorBase(final Object baseParams) {

        if (!(baseParams instanceof Map)) {
            return false;
        }

        Map<String, Object> baseData = (Map<String, Object>) baseParams;

        if (!(baseData.get("BaseTerminalSize") instanceof Integer)) {
            return false;
        }

        return (baseData.get("AmountOfCars") instanceof Integer);
    }

    public boolean validatorQueue(final Object queueParams) {

        if (!(queueParams instanceof Map)) {
            return false;
        }

        Map<String, Object> queueData = (Map<String, Object>) queueParams;

        if (!(queueData.get("Queue") instanceof Integer)) {
            return false;
        }

        if (!(queueData.get("MinTerminalSize") instanceof Integer)) {
            return false;
        }

        return (queueData.get("MaxTerminalSize") instanceof Integer);

    }
}
