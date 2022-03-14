package deb.kalmh.jpa.handler;

import deb.kalmh.jpa.exception.BaseException;
import deb.kalmh.jpa.exception.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

// 여러 Controller에 흩어져 있는 ExceptionHandler를 한 곳으로 모으기 위한 어노테이션
// Bean으로 등록 가능한 Componenet의 일종
// ExceptionHandler와 다르게 종속적이지 않음.
// @RestControllerAdvice가 존재 -> @ResponseBody 어노테이션 생략 가능
@RestControllerAdvice
public class PostControllerAdvice {
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleException(BaseException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto handleValidException(
            MethodArgumentNotValidException exception
    ) {
        return new ErrorResponseDto(exception.getMessage());
    }
}
