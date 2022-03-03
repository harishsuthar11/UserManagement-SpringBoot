package com.example.UserAPI.repository;

import com.example.UserAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
//    @Query("SELECT u from user u where u.mobilenumber=?1")
     User findByMobilenumber(String mobilenumber);
     List<User> findByUsernameContaining(String keyword);
     User findByUsername(String username);


}
