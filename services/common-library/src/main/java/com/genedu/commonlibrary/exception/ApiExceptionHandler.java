package com.genedu.commonlibrary.exception;

import com.genedu.commonlibrary.exception.DTO.error.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";
    private static final String INVALID_REQUEST_INFORMATION_MESSAGE = "Request information is not valid";

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDTO> handleOtherException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();
        return buildErrorResponse(status, message, null, ex, request, 500);
    }

    private String getServletPath(WebRequest webRequest) {
        ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
        return servletRequest.getRequest().getServletPath();
    }

    private ResponseEntity<ErrorDTO> buildErrorResponse(HttpStatus status, String message, List<String> errors,
                                                       Exception ex, WebRequest request, int statusCode) {
        ErrorDTO errorVm =
                new ErrorDTO(status.toString(), status.getReasonPhrase(), message, errors);

        if (request != null) {
            log.error(ERROR_LOG_FORMAT, this.getServletPath(request), statusCode, message);
        }
        log.error(message, ex);
        return ResponseEntity.status(status).body(errorVm);
    }
}
