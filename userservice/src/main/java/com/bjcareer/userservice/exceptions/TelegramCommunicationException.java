package com.bjcareer.userservice.exceptions;

public class TelegramCommunicationException extends RuntimeException{
    public TelegramCommunicationException() {
    }

    public TelegramCommunicationException(String message) {
        super(message);
    }

    public TelegramCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramCommunicationException(Throwable cause) {
        super(cause);
    }

    public TelegramCommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
