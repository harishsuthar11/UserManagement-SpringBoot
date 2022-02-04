package com.example.UserAPI.repository;

import com.example.UserAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
//    @Query("SELECT u from user u where u.mobilenumber=?1")
     public User findByMobilenumber(String mobilenumber);
     List<User> findByUsernameContaining(String keyword);
     User findByUsername(String username);


}
