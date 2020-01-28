package by.epam.shpakova.parser;

import by.epam.shpakova.exception.ParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseParser {
    private static final Logger logger= LogManager.getLogger();

    public Map<String, Object> parseLine(String line)
            throws ParserException {

        Map<String, Object> result = new HashMap<>();
        List<Object> paramValues = null;
        boolean isInArray = false;

        line = line.trim();

        if (line.charAt(0) == '[') {
            isInArray = true;
            line = unwrap(line, '[', ']');
            paramValues = new ArrayList<>();
            result.put("values", paramValues);
        } else {
            line = unwrap(line, '{', '}');  //возвращает line, кот. начинается со скобки и заканч. скобкой
        }

        while (line.length() > 0) {

            String paramName = "";
            Object paramValue;
            line = line.trim();

            if (!isInArray) {
                int closingQuoteIndex = findClosingQuote(line);
                int colonIndex = line.indexOf(':', closingQuoteIndex);      //двоеточие
                if (colonIndex <= 0) {
                    logger.error("Colon not found at: ");
                    throw new ParserException ("Colon not found at: " + line);
                }
                paramName = unwrap(line.substring(0, colonIndex),
                        '"', '"');
                line = line.substring(colonIndex + 1).trim();
            }

            int valueEndIndex;

            if (line.charAt(0) == '{') {
                int closingBraceIndex = findParamClosingBrace(line,
                        '{', '}');
                paramValue
                        = parseLine(line.substring(0, closingBraceIndex + 1));
                valueEndIndex = line.indexOf(',', closingBraceIndex);

                if (valueEndIndex < 0) {
                    valueEndIndex = closingBraceIndex;
                }

            } else if (line.charAt(0) == '[') {
                int closingBraceIndex = findParamClosingBrace(line,
                        '[', ']');
                paramValue = parseLine(line.substring(0, closingBraceIndex
                        + 1)).get("values");
                valueEndIndex = line.indexOf(',', closingBraceIndex);

                if (valueEndIndex < 0) {
                    valueEndIndex = closingBraceIndex;
                }

            } else if (line.charAt(0) == '"') {
                int closingQuoteIndex = findClosingQuote(line);
                paramValue = unwrap(line.substring(0, closingQuoteIndex + 1),
                        '"', '"');
                valueEndIndex = line.indexOf(',', closingQuoteIndex);

                if (valueEndIndex < 0) {
                    valueEndIndex = closingQuoteIndex;
                }

            } else {
                valueEndIndex = line.indexOf(',');
                if (valueEndIndex < 0) {
                    valueEndIndex = line.length();
                }
                paramValue
                        = parseValue(line.substring(0, valueEndIndex).trim());
            }

            if (!isInArray) {
                result.put(paramName, paramValue);
            } else {
                paramValues.add(paramValue);
            }

            if (valueEndIndex >= line.length()) {
                break;
            }

            line = line.substring(valueEndIndex + 1);
        }
        return result;
    }

    private String unwrap(String s, char start, char end)     //Обрезать пробелы и удалить начальные и конечные символы строки
            throws ParserException{

        s = s.trim();

        if (s.charAt(0) != start || s.charAt(s.length() - 1) != end) {
            logger.error("Error of wrapping : ");
            throw new ParserException("Error of wrapping : " + s);
        }

        return s.substring(1, s.length() - 1).trim();
    }


    private int findClosingQuote(String s) throws ParserException {

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '"' && s.charAt(i) != '\\') {
                return i;
            }
        }

        logger.error("Closing quote not found at: " ,s);
        throw new ParserException ("Closing quote not found" + s);
    }

    private int findParamClosingBrace(String s,                           //Найти закрывающую скобку и открывающую
                                      char openBrace, char closingBrace)
            throws ParserException {

        int openBraceCounter = 0;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == openBrace) {
                openBraceCounter++;
            } else if (s.charAt(i) == closingBrace) {
                if (openBraceCounter == 0) {
                    return i;
                } else {
                    openBraceCounter--;
                }
                if (openBraceCounter < 0) {
                    logger.error("More closing braces than open braces at: ", s);
                    throw new ParserException( s);
                }

            }
        }
        String message = "Closing brace not found at: ";
        logger.error("Closing brace not found at: ", s);
        throw new ParserException(s);
    }

    private Object parseValue(String paramValue) throws ParserException { //числ. значения

        try {
            if ("null".equals(paramValue)) {
                return null;
            } else if ("true".equals(paramValue)
                    || "false".equals(paramValue)) {
                return Boolean.valueOf(paramValue);
            } else if (paramValue.indexOf('.') < 0
                    && paramValue.indexOf('e') < 0
                    && paramValue.indexOf('E') < 0) {
                return Integer.valueOf(paramValue);
            } else {
                return Double.valueOf(paramValue);
            }
        } catch (NumberFormatException e) {
            logger.error("Number format exception parsing: ",paramValue, e);
            throw new ParserException(paramValue, e);
        }
    }

}
