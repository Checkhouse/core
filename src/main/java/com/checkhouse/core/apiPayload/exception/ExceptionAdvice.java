package com.checkhouse.core.apiPayload.exception;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.code.ErrorReasonDTO;
import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
@RequiredArgsConstructor
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage =
                e.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(
                e, ErrorStatus.valueOf(errorMessage), request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            String fieldName = fieldError.getField();
            String errorMessage =
                    Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
            errors.merge(
                    fieldName,
                    errorMessage,
                    (existingErrorMessage, newErrorMessage) ->
                            existingErrorMessage + ", " + newErrorMessage);
        }

        return handleExceptionInternalArgs(
                e, ErrorStatus.valueOf("_BAD_REQUEST"), request, errors);
    }

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> onThrowException(
            GeneralException generalException, HttpServletRequest request) {
        ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception e, ErrorReasonDTO reason, HttpHeaders headers, HttpServletRequest request) {

        BaseResponse<Object> body =
                BaseResponse.onFailure(reason.getCode(), reason.getMessage(), null);

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(e, body, headers, reason.getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(
            Exception e,
            ErrorStatus errorCommonStatus,
            WebRequest request,
            Map<String, String> errorArgs) {
        BaseResponse<Object> body =
                BaseResponse.onFailure(
                        errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorArgs);
        return super.handleExceptionInternal(
                e, body, HttpHeaders.EMPTY, errorCommonStatus.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(
            Exception e, ErrorStatus errorCommonStatus, WebRequest request) {
        BaseResponse<Object> body =
                BaseResponse.onFailure(
                        errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
        return super.handleExceptionInternal(
                e, body, HttpHeaders.EMPTY, errorCommonStatus.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalMessage(
            Exception e, HttpHeaders headers, WebRequest request, String errorMessage) {
        ErrorStatus errorStatus = ErrorStatus._BAD_REQUEST;
        BaseResponse<String> body =
                BaseResponse.onFailure(
                        errorStatus.getCode(), errorStatus.getMessage(), errorMessage);

        return super.handleExceptionInternal(
                e, body, headers, errorStatus.getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        String errorMessage = e.getPropertyName() + ": 올바른 값이 아닙니다.";

        return handleExceptionInternalMessage(e, headers, request, errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        String errorMessage = e.getParameterName() + ": 올바른 값이 아닙니다.";

        return handleExceptionInternalMessage(e, headers, request, errorMessage);
    }
}
