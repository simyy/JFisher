package cn.simyy.jfisher.core;

import cn.simyy.jfisher.enums.ErrorEnum;

/**
 * 框架异常 (to break logic)
 *
 * override fillInStackTrace and initCause to improve performance
 */
public class FisherException extends RuntimeException {

    private ErrorEnum error;
    private String msg;

    private FisherException() {}

    private FisherException(String msg) {
    }

    public FisherException(ErrorEnum error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    public static FisherException make(ErrorEnum error, String msg) {
        throw new FisherException(error, msg);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public Throwable initCause(Throwable cause) {
        return this;
    }

    @Override
    public String toString() {
        return String.format("[%d, %s]", error.getCode(), msg);
    }
}
