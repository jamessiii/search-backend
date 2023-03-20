package com.james.core.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Search extends BaseEntity{

    @Id
    @ToString.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /* 검색어 */
    private String keyword;

    /* 검색횟수 */
    private Long callCount;

    public Search(String keyword) {
        this.keyword = keyword;
        this.callCount = 1L;
    }
}
