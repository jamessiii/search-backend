package com.james.api.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SortEnum {

    ACCURACY("accuracy", "sim", "정확도순"),
    RECENCY("recency", "date", "최신순");

    private final String codeKakao;
    private final String codeNaver;
    private final String name;

    public String getCodeKakao() {
        return codeKakao;
    }

    public String getCodeNaver() {
        return codeNaver;
    }

}
