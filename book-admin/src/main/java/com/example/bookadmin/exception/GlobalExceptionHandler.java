package com.example.bookadmin.exception;

import com.example.bookadmin.common.R;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常（@Valid/@Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleValidationException(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return R.fail("参数错误：" + msg);
    }

    /**
     * 处理自定义异常（可选）
     */
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException ex) {
        return R.fail(ex.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace(); // 可用日志记录
        return R.fail("系统运行异常：" + ex.getMessage());
    }

    /**
     * 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception ex) {
        ex.printStackTrace(); // 可用日志记录
        return R.fail("服务器内部错误：" + ex.getMessage());
    }
}
