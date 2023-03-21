package com.james.core.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Shedlock {

    @Id
    @Column(length = 64)
    private String name;

    @Column
    private String lockedBy;

    @CreatedDate
    @Column(length = 3)
    private LocalDateTime lockedAt;

    @Column(length = 3)
    private LocalDateTime lockUntil;
}
