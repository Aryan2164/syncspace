package com.syncspace.backend.repository;

import com.syncspace.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

     @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
     boolean existsByEmail(@Param("email") String email);

     User findByEmail(String email);
}