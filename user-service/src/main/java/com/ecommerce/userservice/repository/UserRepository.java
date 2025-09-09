package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserName(String username);

    Optional<UserEntity> findByUserName(String username);
}
