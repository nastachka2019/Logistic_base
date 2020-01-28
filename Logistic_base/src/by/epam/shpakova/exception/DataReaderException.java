package by.epam.shpakova.exception;

public class DataReaderException extends Exception {
    public DataReaderException() {
    }

    public DataReaderException(String message) {
        super(message);
    }

    public DataReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataReaderException(Throwable cause) {
        super(cause);
    }

    public DataReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
