package boursier.louis.hermeez.backend.apierror;

import boursier.louis.hermeez.backend.apierror.registrationerror.EmailAlreadyTakenException;
import boursier.louis.hermeez.backend.apierror.routeoperationserror.OSRMQueryException;
import boursier.louis.hermeez.backend.apierror.routeoperationserror.OSRMResponseException;
import boursier.louis.hermeez.backend.apierror.signinerror.WrongCredentialsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles exceptions for all controllers with @UserController or @RestController.
 * The implementations of overridden exceptions and custom (@ExceptionHandler) exceptions are found here.
 * We specify that Spring should use this implementation first with @Order(Ordered.HIGHEST_PRECEDENCE).
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    // Overrides the ResponseEntityExceptionHandler Log LOGGER from super class
    private static final Logger LOGGER = LogManager.getLogger(CustomRestExceptionHandler.class);

    private static final String ERROR_OCCURRED_MSG = "error occurred";

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final String error = ex.getRequestPartName() + " part is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        LOGGER.error(ex.getClass().getName());
        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex) {
        LOGGER.error(ex.getClass().getName());
        final List<String> errors = new ArrayList<String>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LOGGER.error(ex.getClass().getName());
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<Object> handleEmailAlreadyTakenException(final EmailAlreadyTakenException ex) {
        LOGGER.error(ex.getClass().getName());
        LOGGER.error(ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), ERROR_OCCURRED_MSG);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({WrongCredentialsException.class})
    public ResponseEntity<Object> handleWrongCredentialsException(final WrongCredentialsException ex) {
        LOGGER.error(ex.getClass().getName());
        LOGGER.error(ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ERROR_OCCURRED_MSG);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex) {
        LOGGER.error(ex.getClass().getName());
        LOGGER.error("UNAUTHORIZED", ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ERROR_OCCURRED_MSG);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({OSRMResponseException.class})
    public ResponseEntity<Object> handleOSRMResponseException(final OSRMResponseException ex) {
        LOGGER.error(ex.getClass().getName());
        LOGGER.error("OSRM server response error", ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ERROR_OCCURRED_MSG);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({OSRMQueryException.class})
    public ResponseEntity<Object> handleOSRMQueryException(final OSRMQueryException ex) {
        LOGGER.error(ex.getClass().getName());
        LOGGER.error("OSRM server query error", ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ERROR_OCCURRED_MSG);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    // Parsing error from JSON to Java object
    @ExceptionHandler({JsonProcessingException.class})
    public ResponseEntity<Object> handleOSRMQueryException(final JsonProcessingException ex) {
        LOGGER.error(ex.getClass().getName());
        LOGGER.error("Parsing error from JSON OSRM Route object to Java object", ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ERROR_OCCURRED_MSG);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 500
    // Generic fall back handler

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex) {
        LOGGER.error(ex.getClass().getName());
        LOGGER.error("error", ex);
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ERROR_OCCURRED_MSG);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}