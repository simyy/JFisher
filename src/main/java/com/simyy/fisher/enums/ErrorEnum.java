package com.simyy.fisher.enums;

/**
 * Created by yu on 17/4/9.
 */
public enum ErrorEnum {
    PARAM(0),
    INNER(1);

    private int code;

    private ErrorEnum(int code) {
        this.code = code;
    }
}
