package com.james.core.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Search extends BaseEntity{

    @Id
    @Column
    @ToString.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /* 검색어 */
    @Column(unique = true)
    private String keyword;

    /* 검색횟수 */
    @Column(nullable = false)
    private Long callCount;

    public Search(String keyword) {
        this.keyword = keyword;
        this.callCount = 1L;
    }
}
