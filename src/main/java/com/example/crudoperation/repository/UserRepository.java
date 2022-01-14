package com.example.crudoperation.repository;

import com.example.crudoperation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean findByEmail(String email);

}
