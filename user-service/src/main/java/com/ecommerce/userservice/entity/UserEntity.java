package com.ecommerce.userservice.entity;

import com.ecommerce.userservice.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity{

    @Column(name = "user_name", nullable = false, unique = true)
    String userName;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String phone;

    @Column(nullable = false)
    String address;

    @CreationTimestamp
    @Column(nullable = false)
    Timestamp created;

    @UpdateTimestamp
    @Column(nullable = false)
    Timestamp updated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role;

    boolean blocked;

}
