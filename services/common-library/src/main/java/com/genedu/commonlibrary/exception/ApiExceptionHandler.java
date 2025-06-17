package com.genedu.commonlibrary.exception;

import com.genedu.commonlibrary.exception.DTO.error.ErrorDTO;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return handleBadRequest(ex, request);
    }

    /**
     * Handles {@link ConstraintViolationException} thrown when validation fails
     * on method parameters annotated with {@code @RequestParam}, {@code @PathVariable},
     * or during manual validation using the {@code Validator} API.
     *
     * <p>This method collects all constraint violations and returns them as a list of
     * human-readable error messages in the response body.</p>
     *
     * @param ex the ConstraintViolationException containing validation error details
     * @return a ResponseEntity containing an ErrorDTO with BAD_REQUEST status and validation messages
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolation(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> String.format("%s %s: %s",
                        violation.getRootBeanClass().getName(),
                        violation.getPropertyPath(),
                        violation.getMessage()))
                .toList();

        return buildErrorResponse(status, INVALID_REQUEST_INFORMATION_MESSAGE, errors, ex, null, 0);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} thrown when validation fails
     * on a {@code @RequestBody} object annotated with {@code @Valid} or {@code @Validated}.
     *
     * <p>This method extracts all field-specific validation errors from the request body and returns them
     * as a list of user-friendly error messages in the response.</p>
     *
     * @param ex the MethodArgumentNotValidException containing binding result errors
     * @param request the current web request
     * @return a ResponseEntity containing an ErrorDTO with BAD_REQUEST status and field error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                   WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .toList();

        return buildErrorResponse(status, INVALID_REQUEST_INFORMATION_MESSAGE, errors, ex, request, 0);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request, 404);
    }

    @ExceptionHandler(DuplicatedException.class)
    protected ResponseEntity<ErrorDTO> handleDuplicated(DuplicatedException ex) {
        return handleBadRequest(ex, null);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    protected ResponseEntity<ErrorDTO> handleInternalServerErrorException(InternalServerErrorException e) {
        log.error("Internal server error exception: ", e);
        ErrorDTO errorVm = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
        return ResponseEntity.internalServerError().body(errorVm);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorDTO> handleMissingParams(MissingServletRequestParameterException e) {
        return handleBadRequest(e, null);
    }

    private String getServletPath(WebRequest webRequest) {
        ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
        return servletRequest.getRequest().getServletPath();
    }

    private ResponseEntity<ErrorDTO> handleBadRequest(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request, 400);
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
