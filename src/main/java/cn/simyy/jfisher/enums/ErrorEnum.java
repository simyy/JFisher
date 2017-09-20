package cn.simyy.jfisher.enums;

/**
 * Created by yu on 17/4/9.
 */
public enum ErrorEnum {
    PARAM(0, "参数错误"),
    INNER(1, "内部错误"),
    INVALID(2, "无效错误");

    private int code;
    private String msg;

    private ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
