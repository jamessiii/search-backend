package com.james.core.domain.entity;

import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Search extends BaseEntity{

    @Id
    @ToString.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /* 검색어 */
    private String keyword;

    /* 검색횟수 */
    private Long callCount;
}
