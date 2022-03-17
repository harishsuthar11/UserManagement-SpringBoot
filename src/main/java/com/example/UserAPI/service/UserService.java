package com.example.UserAPI.service;


import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.exception.BadRequestException;
import com.example.UserAPI.exception.ResourceNotFoundException;
import com.example.UserAPI.model.User;

import com.example.UserAPI.repository.UserRepository;

import com.example.UserAPI.validation.Validation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validation validation;

    private static final Logger logger = Logger.getLogger(UserService.class);

    public List<User> getUsers(int pageNumber,int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        return userRepository.findAll(pageable).getContent();

    }
    public User getUserById(Long id){

        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User ID Not exist"));

        return user;

    }
    @Transactional
    public ResponseObject createUser(User user){

        user.setStatus("ACTIVE");
        user.setCreateddate(new Date());
        user.setActiveWallet(false);
        if(userRepository.findByUsername(user.getUsername())!=null||userRepository.findByMobilenumber(user.getMobilenumber())!=null){
            logger.warn("Username or Mobile Number already exist");
            return new ResponseObject(HttpStatus.BAD_REQUEST,"User Already Exist");
        }
        try{
            userRepository.save(user);
            logger.info("User Created with Name:"+user.getFirstname()+" "+user.getLastname());
            return new ResponseObject(HttpStatus.CREATED,"User Created Successfully");
        }
        catch (Exception exception){
            throw new BadRequestException("User Can't Be Created "+user);
        }

    }
    public void updateUser(User user){

        try{

            userRepository.save(user);
            logger.info("Updated User "+user);

        }
        catch (Exception exception){
            throw  new RuntimeException("User Can't Be Updated");
        }



    }
    @Transactional
    public User findByMobileno(String mobilenumber){

        User user = userRepository.findByMobilenumber(mobilenumber);

        return user;

    }
    public List<User> getUsersByKeyword(String keyword){

        List<User> userList = userRepository.findByUsernameContaining(keyword);

        return userList;

    }
    public User findByUsername(String username){

        return userRepository.findByUsername(username);

    }

    public void deleteUser(long id){

        userRepository.deleteById(id);

    }

    public ResponseObject validate(User user){

        if(!validation.emailValidation(user.getEmail())) {

            logger.debug("Email  is Not Valid");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Email Invalid");

        }

        if(!validation.mobileNumberValidate(user.getMobilenumber())){

            logger.debug("Mobile Number is Invalid");
            return new ResponseObject(HttpStatus.BAD_REQUEST,"Invalid Mobile Number");

        }
        return new ResponseObject(HttpStatus.OK,"Email and Mobile Number are  Correct");

    }

}
