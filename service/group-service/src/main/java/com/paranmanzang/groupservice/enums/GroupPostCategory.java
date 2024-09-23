package com.paranmanzang.groupservice.enums;

public enum GroupPostCategory implements CodeEnum<String>{
    NOTICE("NOTICE"), GENERAL("GENERAL");

    private final String code;

    GroupPostCategory(String code){
        this.code = code;
    }
    @Override
    public String getCode() {
        return code;
    }
}
