package com.powerpuff.billServer.model;

public enum UsingType {
    ACTIVE(0),     // 活跃状态
    INACTIVE(1),   // 非活跃状态
    DELETED(2);    // 已删除状态

    private final int value;

    UsingType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UsingType fromValue(int value) {
        for (UsingType type : UsingType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UsingType value: " + value);
    }
}

