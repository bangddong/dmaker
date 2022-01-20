package com.fastcampus.programming.dmaker.exception;

import com.fastcampus.programming.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.INVALID_REQUEST;

/**
 * 공통 에러처리 핸들러
 */
@RestControllerAdvice
@Slf4j
public class DMakerExceptionHandler {

    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException (DMakerException e, HttpServletRequest request) {
        log.error("errorCode: {}, url: {}, message: {}",
                e.getDMakerErrorCde(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCde())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class, // 요청타입 매칭 에러 (GET을 POST로..)
            MethodArgumentNotValidException.class // @Valid 에서 나오는 에러 핸들링
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
