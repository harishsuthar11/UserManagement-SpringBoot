package com.example.crudoperation.repository;

import com.example.crudoperation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
   // User findByEmail(String email);
//   public List<User> findByEmailOrMobilenumber(String email, String mobile);


}
