package com.cocola.gpt.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GptException extends RuntimeException {
    private int errorCode;
    private String errorMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public GptException() {
    }

    public GptException(String message) {
        super(message);
    }

    public GptException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


    @Override
    public String getMessage() {
        return this.errorMessage;
    }

    /**
     * 自定义异常不打印堆栈信息
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
