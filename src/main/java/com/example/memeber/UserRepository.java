package com.example.memeber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    // JPA 메서드 쿼리
    @Query(value = "SELECT * FROM multiple_users WHERE email LIKE :prefix ORDER BY id DESC", nativeQuery = true)
    List<User> findUsersByEmailPrefix(@Param("prefix") String prefix);
}