package com.james.api.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SortEnum {

    ACCURACY("accuracy", "정확도순"),
    RECENCY("recency", "최신순");

    private final String code;
    private final String name;

    public String getCode() {
        return code;
    }

}
