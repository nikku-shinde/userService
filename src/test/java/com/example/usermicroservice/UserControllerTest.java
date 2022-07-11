package com.example.usermicroservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.HttpClientErrorException.Conflict;
import org.springframework.web.client.HttpServerErrorException.BadGateway;

import com.example.usermicroservice.config.CustomUserDetailsService;
import com.example.usermicroservice.config.JwtUtil;
import com.example.usermicroservice.controller.UserController;
import com.example.usermicroservice.dto.RoleDTO;
import com.example.usermicroservice.dto.UserDTO;
import com.example.usermicroservice.entity.RoleModel;
import com.example.usermicroservice.entity.UserData;
import com.example.usermicroservice.exception.UserNotFoundException;
import com.example.usermicroservice.helper.Course;
import com.example.usermicroservice.helper.Questions;
import com.example.usermicroservice.helper.SubTopic;
import com.example.usermicroservice.helper.Topics;
import com.example.usermicroservice.payload.EmailPayload;
import com.example.usermicroservice.payload.ForgotPasswordPayload;
import com.example.usermicroservice.payload.LoginPayload;
import com.example.usermicroservice.payload.OtpPayload;
import com.example.usermicroservice.repository.UserRepository;
import com.example.usermicroservice.service.UserService;
import com.example.usermicroservice.util.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = {UserControllerTest.class})
class UserControllerTest {

	@Mock
	private UserService service;
	
	@Mock
	private UserRepository userRepo;
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private CustomUserDetailsService userDetailsService;
	
	@Mock
	private JwtUtil jwtUtil;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	
	@Test
	@Order(1)
	void test_getAllUsers() {
		List<UserData> user_list = UserDummyData.getUserList();
		when(service.getAllUsers()).thenReturn(user_list);
		ResponseEntity<List<UserData>> res = userController.getAllUsers();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	
	@Test
	@Order(24)
	void test_getAllUsersThrowException() throws Exception {
		when(service.getAllUsers()).thenThrow(NullPointerException.class);
		ResponseEntity<List<UserData>> res = userController.getAllUsers();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "User list not found");
	}
	
	@Test
	@Order(2)
	void test_getUserByUserName() {
		UserData user = UserDummyData.getUser();
		when(service.getUserByUserName(user.getUserName())).thenReturn(user);
		ResponseEntity<UserData> res = userController.getUserByUserName(user.getUserName());
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(user, res.getBody());
	}
	
	@Test
	@Order(25)
	void test_getUserByUserNameThrowException() throws Exception {
		UserData user = UserDummyData.getUser();
		when(service.getUserByUserName(user.getUserName())).thenThrow(NullPointerException.class);
		ResponseEntity<UserData> res = userController.getUserByUserName(user.getUserName());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "User not found");
	}
	
	@Test
	@Order(3)
	void test_getUserById() {
		UserData user = UserDummyData.getUser();
		Long userId = 1l;
		when(service.getUserById(userId)).thenReturn(user);
		ResponseEntity<UserData> res = userController.getUserById(userId);
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(user, res.getBody());
	}
	
	@Test
	@Order(26)
	void test_getUserByIdThrowException() throws Exception {
		UserData user = UserDummyData.getUser();
		when(service.getUserById(user.getId())).thenThrow(NullPointerException.class);
		ResponseEntity<UserData> res = userController.getUserById(user.getId());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "User not found");
	}
	
	@Test
	@Order(4)
	void test_addUserData() {
		UserData user = UserDummyData.getUser();
		UserDTO userDTO = UserDummyData.getUserDTO();
		when(service.addUserData(userDTO)).thenReturn(user);
		ResponseEntity<UserData> res = userController.addUserData(userDTO);
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(user, res.getBody());
	}
	
	@Test
	@Order(27)
	void test_addUserDataThrowException() throws Exception {
		UserDTO userDTO = UserDummyData.getUserDTO();
		when(service.addUserData(userDTO)).thenThrow(Conflict.class);
		ResponseEntity<UserData> res = userController.addUserData(userDTO);
		Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode() , "Conflict occurs");
	}
	
	@Test
	@Order(5)
	void test_deleteUser() {
		Long userId = 1l;
		String message = Constants.USER_DELETED_SUCCESSFULLY;
		when(service.deleteUser(userId)).thenReturn(message);
		ResponseEntity<Object> res = userController.deleteUser(userId);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(Constants.USER_DELETED_SUCCESSFULLY, res.getBody());
	}
	
	@Test
	@Order(49)
	void test_deleteUserThrowException() throws Exception {
		Long userId = 1l;
		when(service.deleteUser(userId)).thenThrow(NullPointerException.class);
		ResponseEntity<Object> res = userController.deleteUser(userId);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "User not found");
	}
	
	@Test
	@Order(50)
	void test_getUser() {
		List<String> users = new ArrayList<String>();
		users.add("abc");
		users.add("abc@gmail.com");
		users.add("abc@123");
		users.add("abc@123");
		users.add("Java");
		users.add("ROLE_ADMIN");
		when(service.getUser()).thenReturn(users);
		ResponseEntity<List<String>> res = userController.getUser();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(6, res.getBody().size());
	}
	
	@Test
	@Order(51)
	void test_getUserThrowException() throws Exception {
		when(service.getUser()).thenThrow(NullPointerException.class);
		ResponseEntity<List<String>> res = userController.getUser();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "User list not found");
	}
	
	@Test
	@Order(6)
	void test_updateUser() {
		UserDTO userDTO = UserDummyData.getUserDTO();
		Long userId = 1l;
		UserData user = UserDummyData.getUser();
		when(service.updateUser(userId, userDTO)).thenReturn(user);
		ResponseEntity<UserData> res = userController.updateUser(userId, userDTO);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(user, res.getBody());
	}
	
	@Test
	@Order(28)
	void test_updateUserThrowException() throws Exception {
		UserDTO userDTO = UserDummyData.getUserDTO();
		Long userId = 1l;
		when(service.updateUser(userId, userDTO)).thenThrow(Conflict.class);
		ResponseEntity<UserData> res = userController.updateUser(userId, userDTO);
		Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode() , "Conflict occurs");
	}
	
	@Test
	@Order(7)
	void test_getAllRoles() {
		UserData user = UserDummyData.getUser();
		List<RoleModel> roleList = user.getRoles();
		when(service.getAllRoles()).thenReturn(roleList);
		ResponseEntity<List<RoleModel>> res = userController.getAllRoles();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(1, res.getBody().size());
	}
	
	@Test
	@Order(29)
	void test_getAllRolesThrowException() throws Exception {
		when(service.getAllRoles()).thenThrow(NullPointerException.class);
		ResponseEntity<List<RoleModel>> res = userController.getAllRoles();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "Roles list not found");	
	}
	
	@Test
	@Order(8)
	void test_addRoles() {
		RoleDTO roleDTO = UserDummyData.getRoleDTO();
		RoleModel role = UserDummyData.getRole();
		when(service.addRoles(roleDTO)).thenReturn(role);
		ResponseEntity<RoleModel> res = userController.addRoles(roleDTO);
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(role, res.getBody());
	}
	
	@Test
	@Order(30)
	void test_addRolesThrowsException() throws Exception {
		RoleDTO roleDTO = UserDummyData.getRoleDTO();
		when(service.addRoles(roleDTO)).thenThrow(Conflict.class);
		ResponseEntity<RoleModel> res = userController.addRoles(roleDTO);
		Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode() , "Conflict occurs");
	}
	
	@Test
	@Order(9)
	void test_addCourse() {
		Course course = UserDummyData.getCourse();
		when(service.addCourse(course)).thenReturn(course);
		ResponseEntity<Course> res = userController.addCourse(course);
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(course, res.getBody());
	}
	
	@Test
	@Order(31)
	void test_addCourseThrowException() throws Exception {
		Course course = UserDummyData.getCourse();
		when(service.addCourse(course)).thenThrow(Conflict.class);
		ResponseEntity<Course> res = userController.addCourse(course);
		Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode() , "Conflict occurs");
	}
	
	@Test
	@Order(10)
	void test_addTopics() {
		Topics topic = UserDummyData.getTopic();
		when(service.addTopics(topic)).thenReturn(topic);
		ResponseEntity<Topics> res = userController.addTopics(topic);
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(topic, res.getBody());
	}
	
	@Test
	@Order(32)
	void test_addTopicsThrowException() throws Exception {
		Topics topic = UserDummyData.getTopic();
		when(service.addTopics(topic)).thenThrow(Conflict.class);
		ResponseEntity<Topics> res = userController.addTopics(topic);
		Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode() , "Conflict occurs");
	}
	
	@Test
	@Order(11)
	void test_addSubTopics() {
		SubTopic subTopic = UserDummyData.getSubTopic();
		when(service.addSubTopics(subTopic)).thenReturn(subTopic);
		ResponseEntity<SubTopic> res = userController.addSubTopics(subTopic);
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(subTopic, res.getBody());
	}
	
	@Test
	@Order(33)
	void test_addSubTopicsThrowException() throws Exception {
		SubTopic subTopic = UserDummyData.getSubTopic();
		when(service.addSubTopics(subTopic)).thenThrow(Conflict.class);
		ResponseEntity<SubTopic> res = userController.addSubTopics(subTopic);
		Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode() , "Conflict occurs");
	}
	
	@Test
	@Order(12)
	void test_addQuestions() {
		Questions question = UserDummyData.getQuestion();
		when(service.addQuestions(question)).thenReturn(question);
		ResponseEntity<Questions> res = userController.addQuestions(question);
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(question, res.getBody());
	}
	
	@Test
	@Order(34)
	void test_addQuestionsThrowException() throws Exception {
		Questions question = UserDummyData.getQuestion();
		when(service.addQuestions(question)).thenThrow(Conflict.class);
		ResponseEntity<Questions> res = userController.addQuestions(question);
		Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode() , "Conflict occurs");
	}
	
	@Test
	@Order(13)
	void test_getCourseNames() {
		List<Course> courseList = UserDummyData.getCourseList();
		when(service.getCourseNames()).thenReturn(courseList);
		ResponseEntity<List<Course>> res = userController.getCourseNames();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	
	@Test
	@Order(35)
	void test_getCourseNamesThrowException() throws Exception {
		when(service.getCourseNames()).thenThrow(NullPointerException.class);
		ResponseEntity<List<Course>> res = userController.getCourseNames();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "Course list not found");
	}
	
	@Test
	@Order(14)
	void test_getTopics() {
		List<Topics> topicList = UserDummyData.getTopicList();
		when(service.getTopics()).thenReturn(topicList);
		ResponseEntity<List<Topics>> res = userController.getTopics();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	
	@Test
	@Order(36)
	void test_getTopicsThrowException() throws Exception {
		when(service.getTopics()).thenThrow(NullPointerException.class);
		ResponseEntity<List<Topics>> res = userController.getTopics();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "topic list not found");
	}
	
	@Test
	@Order(15)
	void test_getSubTopics() {
		List<SubTopic> subTopicList = UserDummyData.getSubTopicList();
		when(service.getSubTopics()).thenReturn(subTopicList);
		ResponseEntity<List<SubTopic>> res = userController.getSubTopics();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	
	@Test
	@Order(37)
	void test_getSubTopicsThrowException() throws Exception {
		when(service.getSubTopics()).thenThrow(NullPointerException.class);
		ResponseEntity<List<SubTopic>> res = userController.getSubTopics();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "SubTopic list not found");
	}
	
	@Test
	@Order(16)
	void test_getQuestions() {
		List<Questions> questionList = UserDummyData.getQuestionList();
		when(service.getQuestions()).thenReturn(questionList);
		ResponseEntity<List<Questions>> res= userController.getQuestions();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	@Test
	@Order(38)
	void test_getQuestionsThrowException() throws Exception {
		when(service.getQuestions()).thenThrow(NullPointerException.class);
		ResponseEntity<List<Questions>> res= userController.getQuestions();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "Question list not found");
	}
	
	@Test
	@Order(17)
	void test_getTopicByCourseId() {
		Long courseId = 1l;
		List<Topics> topicList = UserDummyData.getTopicList();
		when(service.getTopicByCourseId(courseId)).thenReturn(topicList);
		ResponseEntity<List<Topics>> res = userController.getTopicByCourseId(courseId);
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	
	@Test
	@Order(39)
	void test_getTopicByCourseIdThrowException() throws Exception {
		Long courseId = 1l;
		when(service.getTopicByCourseId(courseId)).thenThrow(NullPointerException.class);
		ResponseEntity<List<Topics>> res = userController.getTopicByCourseId(courseId);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "Topic not found by courseId");
	}
	
	@Test
	@Order(18)
	void test_getSubTopicByTopicId() {
		Long topicId = 1l;
		List<SubTopic> subTopicList = UserDummyData.getSubTopicList();
		when(service.getSubTopicByTopicId(topicId)).thenReturn(subTopicList);
		ResponseEntity<List<SubTopic>> res = userController.getSubTopicByTopicId(topicId);
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	
	@Test
	@Order(40)
	void test_getSubTopicByTopicIdThrowException() throws Exception {
		Long topicId = 1l;
		when(service.getSubTopicByTopicId(topicId)).thenThrow(NullPointerException.class);
		ResponseEntity<List<SubTopic>> res = userController.getSubTopicByTopicId(topicId);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "SubTopic not found by topicId");
	}
	
	@Test
	@Order(19)
	void test_getQuestionsBySubTopicId() {
		Long subTopicId = 1l;
		List<Questions> questionList = UserDummyData.getQuestionList();
		when(service.getQuestionsBySubTopicId(subTopicId)).thenReturn(questionList);
		ResponseEntity<List<Questions>> res = userController.getQuestionsBySubTopicId(subTopicId);
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	@Test
	@Order(41)
	void test_getQuestionsBySubTopicIdThrowException() throws Exception {
		Long subTopicId = 1l;
		when(service.getQuestionsBySubTopicId(subTopicId)).thenThrow(NullPointerException.class);
		ResponseEntity<List<Questions>> res = userController.getQuestionsBySubTopicId(subTopicId);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode(), "Question not found by subTopicId");
	}
	
	@Test
	@Order(20)
	void test_sendEmail() {
		EmailPayload emailPayload = new EmailPayload("abc@gmail.com", "test mail", "test mail");
		String message = Constants.MAIL_SEND_MESSAGE;
		when(service.sendEmail(emailPayload)).thenReturn(message);
		ResponseEntity<Object> res = userController.sendEmail(emailPayload);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(Constants.MAIL_SEND_MESSAGE, res.getBody());
	}
	
	@Test
	@Order(48)
	void test_sendEmailThrowException() throws Exception {
		EmailPayload emailPayload = new EmailPayload("abc@gmail.com", "test mail", "test mail");
		when(service.sendEmail(emailPayload)).thenThrow(BadGateway.class);
		ResponseEntity<Object> res = userController.sendEmail(emailPayload);
		Assertions.assertEquals(HttpStatus.BAD_GATEWAY, res.getStatusCode(), "Bad gateway occurs");
	}
	
	@Test
	@Order(21)
	void test_sendOtp() {
		OtpPayload otpPayload = new OtpPayload("abc@gmail.com");
		Integer otp = 123456;
		when(service.sendOtp(otpPayload)).thenReturn(otp);
		ResponseEntity<Object> res = userController.sendOtp(otpPayload);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(otp, res.getBody());
	}
	
	@Test
	@Order(42)
	void test_sendOtpThrowException() throws Exception {
		OtpPayload otpPayload = new OtpPayload("abc@gmail.com");
		when(service.sendOtp(otpPayload)).thenThrow(BadGateway.class);
		ResponseEntity<Object> res = userController.sendOtp(otpPayload);
		Assertions.assertEquals(HttpStatus.BAD_GATEWAY, res.getStatusCode(), "Bad gateway occurs");
	}
	
	@Test
	@Order(22)
	void test_changePassword() {
		ForgotPasswordPayload forgotPasswordPayload = new ForgotPasswordPayload("abc@gmail.com", "abc@231");
		when(service.changePassword(forgotPasswordPayload)).thenReturn(Constants.PASSWORD_CHANGED_SUCCESSFULLY);
		ResponseEntity<Object> res = userController.changePassword(forgotPasswordPayload);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(Constants.PASSWORD_CHANGED_SUCCESSFULLY, res.getBody());
	}
	
	@Test
	@Order(43)
	void test_changePasswordThrowException() throws Exception {
		ForgotPasswordPayload forgotPasswordPayload = new ForgotPasswordPayload("abc@gmail.com", "abc@231");
		when(service.changePassword(forgotPasswordPayload)).thenThrow(BadGateway.class);
		ResponseEntity<Object> res = userController.changePassword(forgotPasswordPayload);
		Assertions.assertEquals(HttpStatus.BAD_GATEWAY, res.getStatusCode(), "Bad gateway occurs");
	}

	
	@Test
	@Order(23)
	void test_createAuthenticationToken() throws Exception {
		LoginPayload loginPayload = new LoginPayload("abc@123", "abc@123");
		UserData user = UserDummyData.getUser();
		when(userRepo.getUserByUserName(loginPayload.getUserName())).thenReturn(user);
		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		when(passwordEncoder.matches(loginPayload.getPassword(), user.getPassword())).thenReturn(true);
		UserDetails userDetails = new User(loginPayload.getUserName(), loginPayload.getPassword(), roles);
		when(userDetailsService.loadUserByUsername(loginPayload.getUserName())).thenReturn(userDetails);
		String token = "token";
		when(jwtUtil.generateToken(userDetails)).thenReturn(token);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginPayload.getUserName(),loginPayload.getPassword());
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		ResponseEntity<Object> res = userController.createAuthenticationToken(loginPayload);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertNotNull(res.getBody());
	}
	
	@Test
	@Order(44)
	void test_createAuthenticationTokenNegative() throws Exception {
		LoginPayload loginPayload = new LoginPayload("abc@123", "abc@123");
		UserData user = UserDummyData.getUser();
		when(userRepo.getUserByUserName(loginPayload.getUserName())).thenReturn(user);
		when(passwordEncoder.matches(loginPayload.getPassword(), user.getPassword())).thenReturn(false);
		ResponseEntity<Object> res = userController.createAuthenticationToken(loginPayload);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	@Order(45)
	void test_createAuthenticationTokenThrowDisable() throws Exception {
		LoginPayload loginPayload = new LoginPayload("abc@123", "abc@123");
		when(userRepo.getUserByUserName(loginPayload.getUserName())).thenThrow(DisabledException.class);
		ResponseEntity<Object> res = userController.createAuthenticationToken(loginPayload);
		Assertions.assertEquals(HttpStatus.CONFLICT,res.getStatusCode(),"User disabled");
	}
	
	@Test
	@Order(46)
	void test_createAuthenticationTokenThrowBadCredentials() throws Exception {
		LoginPayload loginPayload = new LoginPayload("abc@123", "abc@123");
		when(userRepo.getUserByUserName(loginPayload.getUserName())).thenThrow(BadCredentialsException.class);
		ResponseEntity<Object> res = userController.createAuthenticationToken(loginPayload);
		Assertions.assertEquals(HttpStatus.CONFLICT,res.getStatusCode(),"Invalid user");
	}
	
	@Test
	@Order(47)
	void test_createAuthenticationTokenThrowNullPointer() throws Exception {
		LoginPayload loginPayload = new LoginPayload("abc@123", "abc@123");
		when(userRepo.getUserByUserName(loginPayload.getUserName())).thenThrow(NullPointerException.class);
		ResponseEntity<Object> res = userController.createAuthenticationToken(loginPayload);
		Assertions.assertEquals(HttpStatus.NOT_FOUND,res.getStatusCode(),"User not found");
	}
}
