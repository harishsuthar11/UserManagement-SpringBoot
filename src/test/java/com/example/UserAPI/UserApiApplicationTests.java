package com.example.UserAPI;

import com.example.UserAPI.controller.UserController;
import com.example.UserAPI.dto.JwtRequest;
import com.example.UserAPI.dto.JwtResponse;
import com.example.UserAPI.dto.ResponseObject;
import com.example.UserAPI.model.User;
import com.example.UserAPI.dao.UserRepository;
import com.example.UserAPI.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class)
class UserApiApplicationTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ObjectMapper objectMapper;
	@Mock
	UserService userService;

	User user1;
	JwtRequest jwtRequest;

	@BeforeEach
	void create() throws IOException {
		String userPath = "src/test/java/com/example/UserAPI/json/UserRequest.json";
		String requestUser = new String(Files.readAllBytes(Paths.get(userPath)));
		user1 = new ObjectMapper().readValue(requestUser,User.class);
	}

	public String generateToken() throws Exception{

		//String username = user.getUsername();
		//String password = user.getPassword();

		jwtRequest = new JwtRequest("hs11","123");
		String tokenJson = objectMapper.writeValueAsString(jwtRequest);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(tokenJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		JwtResponse jwtResponse = objectMapper.readValue(content,JwtResponse.class);

		String token = jwtResponse.getJwt();
		return token;



	}

	@Test
	@DisplayName("Create User")
	public  void createuser() throws Exception{

		ResponseObject responseObject = new ResponseObject(HttpStatus.OK,"User Created");

		User user = user1;

		mockMvc.perform(MockMvcRequestBuilders.post("/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(HttpStatus.CREATED)));

	}

//	@Test
//	@Order(1)
//	public void testCreate(){
//		User user = new User();
//		user.setId(6L);
//		user.setUsername("a");
//		user.setFirstname("Harish");
//		user.setLastname("Suthar");
//		user.setStatus("ACTIVE");
//		user.setCreateddate(new Date());
//		user.setMobilenumber("9783472882");
//		user.setAddress1("Jalore");
//		user.setEmail("abc@gmail.com");
//		user.setModifieddate(new Date());
//		user.setAddress2("Bhinmal");
//		userRepository.save(user);
//
//
//
//	}
//	@Test
//	@Order(2)
//	public void testReadAll(){
//		List<User> users = userRepository.findAll();
//
//	}
//	@Test
//	@Order(3)
//	public void testUser(){
//		User user = userRepository.findById(1L).get();
//	}
//	@Test
//	@Order(4)
//	public void testUpdate(){
//		User user = userRepository.findById(1L).get();
//		user.setUsername("aman");
//		user.setModifieddate(new Date());
//		userRepository.save(user);
//	}



}
