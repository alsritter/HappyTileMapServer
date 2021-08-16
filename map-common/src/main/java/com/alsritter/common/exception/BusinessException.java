package com.alsritter.common.exception;

import com.alsritter.common.api.ResultCode;

/**
 * 业务异常处理类
 *
 * @author alsritter
 * @version 1.0
 **/
public class BusinessException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4306448854352716126L;
    private final ResultCode resultCode;

    /**
     * 无参构造函数
     */
    public BusinessException() {
        super();
        this.resultCode = ResultCode.FAILED;
    }

    /**
     * 构造函数
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * 构造函数
     */
    public BusinessException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILED;
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param cause   异常链
     */
    public BusinessException(String message, ResultCode resultCode, Throwable cause) {
        super(message, cause);
        this.resultCode = resultCode;
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public BusinessException(String message, ResultCode resultCode) {
        super(message);
        this.resultCode = resultCode;
    }

    /**
     * 构造函数
     *
     * @param cause 异常链
     */
    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }


    public int getErrorCode() {
        return (int) this.resultCode.getCode();
    }
}
