package com.genedu.commonlibrary.exception;

import com.genedu.commonlibrary.utils.MessagesUtils;

public class InvalidFileFormatException extends RuntimeException {

    private final String message;

    public InvalidFileFormatException(String message) {
        this.message = MessagesUtils.getMessage(message);
    }

    public InvalidFileFormatException(String errorCode, Object... var2) {
        this.message = MessagesUtils.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
