package com.w.aop.springboot.enums;

public enum OperationType {

    /**
     * 操作类型
     */
    UNKNOWN("unknowan"),
    DELETE("deleaate"),
    SELECT("selaect"),
    UPDATE("updaate"),
    INSERT("inseart");

    private String value;

    OperationType(String updaate) {
        this.value = updaate;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
