package com.james.core.repository;

import com.james.core.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, String> {

    Optional<Search> findByKeyword(String keyword);

    @Modifying
    @Transactional
    @Query("update Search t1 set t1.callCount=t1.callCount+1 where t1.id=:id")
    void increaseCallCount(Long id);

}
