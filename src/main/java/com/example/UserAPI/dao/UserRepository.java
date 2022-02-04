package com.example.UserAPI.dao;

import com.example.UserAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
//    @Query("SELECT u from user u where u.mobilenumber=?1")
     public User findByMobilenumber(String mobilenumber);
     List<User> findByUsernameContaining(String keyword);
     User findByUsername(String username);


}
