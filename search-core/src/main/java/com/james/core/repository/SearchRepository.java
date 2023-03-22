package com.james.core.repository;

import com.james.core.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, String> {

    Search findById(Long id);
    Search findByKeyword(String keyword);

    @Modifying
    @Transactional
    @Query("update Search t1 set t1.callCount=t1.callCount+1 where t1.id=:id")
    void increaseCallCount(Long id);

    @Modifying
    @Transactional
    @Query("update Search t1 set t1.callCount=t1.callCount-:count where t1.id=:id")
    void decreaseCallCount(Long id, Long count);

    List<Search> findTop10ByOrderByCallCountDesc();
}
