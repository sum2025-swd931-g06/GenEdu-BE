package com.genedu.commonlibrary.exception;

import com.genedu.commonlibrary.utils.MessagesUtils;

import java.io.IOException;

public class FileStorageException extends RuntimeException {
    private final String message;

    public FileStorageException(String message) {
        this.message = MessagesUtils.getMessage(message);
    }

    public FileStorageException(String errorCode, Object... var2) {
        this.message = MessagesUtils.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
