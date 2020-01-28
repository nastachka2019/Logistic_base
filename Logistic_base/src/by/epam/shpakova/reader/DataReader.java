package by.epam.shpakova.reader;

import by.epam.shpakova.exception.DataReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class DataReader {
    private static final Logger logger = LogManager.getLogger();
    public List<String> readData (String filePath) throws DataReaderException {
        List <String> lines;
        try  (FileReader fileReader = new FileReader(filePath); //поток, который подключается к текстовому файлу
              BufferedReader bufferedReader = new BufferedReader(fileReader)) { // соединяем FileReader с BufferedReader
            lines=bufferedReader.lines().collect(Collectors.toList());  // преобразуем всю инфу в стрим стрингов, получаем коллекцию
        }  catch (IOException e) {
            throw new DataReaderException ("Error: cannot read file." , e);
        }
        return lines;
    }
}
