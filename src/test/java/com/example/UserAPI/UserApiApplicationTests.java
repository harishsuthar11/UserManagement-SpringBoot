package com.example.UserAPI;

import com.example.UserAPI.model.User;
import com.example.UserAPI.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class UserApiApplicationTests {
	@Autowired
	UserRepository userRepository;

	@Test
	public void testCreate(){
		User user = new User();
		user.setId(6L);
		user.setUsername("a");
		user.setFirstname("Harish");
		user.setLastname("Suthar");
		user.setStatus("ACTIVE");
		user.setCreateddate(new Date());
		user.setMobilenumber("9783472882");
		user.setAddress1("Jalore");
		user.setEmail("abc@gmail.com");
		user.setModifieddate(new Date());
		user.setAddress2("Bhinmal");
		userRepository.save(user);



	}
	@Test
	public void testReadAll(){
		List<User> users = userRepository.findAll();

	}
	@Test
	public void testUser(){
		User user = userRepository.findById(1L).get();
	}
	@Test
	public void testUpdate(){
		User user = userRepository.findById(1L).get();
		user.setUsername("aman");
		user.setModifieddate(new Date());
		userRepository.save(user);


	}


}
