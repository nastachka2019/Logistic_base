package by.epam.shpakova.parser;

import by.epam.shpakova.exception.ParserException;
import by.epam.shpakova.exception.ValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataParser {
    private static final Logger logger = LogManager.getLogger();

    public List<Map<String, Object>> parseData(final List<String> readList)
            throws ValidatorException {

        if (readList == null) {
            logger.warn("List is empty");
            throw new ValidatorException("List is empty");
        }

        List<Map<String, Object>> parsedList = new ArrayList<>();
        BaseParser baseParser = new BaseParser();

        for (String string : readList) {
            try {
                parsedList.add(baseParser.parseLine(string));
            } catch (ParserException e) {
                logger.error("Incorrect data in line : " + string);
            }

        }

        return parsedList;
    }
}
