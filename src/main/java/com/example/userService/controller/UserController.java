package com.example.userService.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.userService.DTO.RoleDTO;
import com.example.userService.DTO.UserDTO;
import com.example.userService.config.CustomUserDetailsService;
import com.example.userService.config.JwtUtil;
import com.example.userService.entity.AuthenticationResponse;
import com.example.userService.entity.RoleModel;
import com.example.userService.entity.UserData;
import com.example.userService.exception.UserNotFoundException;
import com.example.userService.helper.Course;
import com.example.userService.helper.Questions;
import com.example.userService.helper.SubTopic;
import com.example.userService.helper.Topics;
import com.example.userService.payload.EmailPayload;
import com.example.userService.payload.ForgotPasswordPayload;
import com.example.userService.payload.LoginPayload;
import com.example.userService.payload.OtpPayload;
import com.example.userService.repository.RoleRepository;
import com.example.userService.repository.UserRepository;
import com.example.userService.service.UserService;
import com.example.userService.util.Constants;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<UserData>> getAllUsers() {
		try {
			List<UserData> users = this.userService.getAllUsers();
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/authenticate/getUserByUserName/{userName}")
	public ResponseEntity<UserData> getUserByUserName(@PathVariable("userName") String userName) {
		return ResponseEntity.ok(this.userService.getUserByUserName(userName));
	}

	@GetMapping("/getUserById/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
		try {
			return ResponseEntity.ok(this.userService.getUserById(id));
		} catch (UserNotFoundException e) {
			return ResponseEntity.ok(new UserNotFoundException("User Not found", e));
		}
	}

	@PostMapping("/authenticate/add-userData")
	public ResponseEntity<Object> addUserData(@RequestBody UserDTO user) {
		try {
			return ResponseEntity.ok(this.userService.addUserData(user));
		} catch (Exception e) {
			return ResponseEntity.ok(new Exception("User Data Invalid", e));
		}
	}

	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id){
		try {
			this.userService.deleteUser(id);
			return ResponseEntity.ok(Constants.USER_DELETED_SUCCESSFULLY);
		} catch (Exception e) {
			return ResponseEntity.ok(new Exception("User cannot delete" ,e));
		}
	}
	
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable("id") Long id , @RequestBody UserDTO userData) {
		try {
			return ResponseEntity.ok(this.userService.updateUser(id, userData));
		} catch (Exception e) {
			return ResponseEntity.ok(new Exception("User cannot  updated",e));
		}
	}
	
	@GetMapping("/authenticate/getAllRoles")
	public ResponseEntity<List<RoleModel>> getAllRoles() {
		return ResponseEntity.ok(this.userService.getAllRoles());
	}
	
	@PostMapping("/authenticate/add-roles")
	public ResponseEntity<Object> addRoles(@RequestBody RoleDTO roles) {
		try {
			return ResponseEntity.ok(this.userService.addRoles(roles));
		} catch (Exception e) {
			return ResponseEntity.ok(new Exception("Role Data Invalid",e));
		}
	}

	@GetMapping("/getUser")
	public ResponseEntity<List<String>> getUser() {
		return ResponseEntity.ok(this.userService.getUser());
	}

	@PostMapping("/add-course")
	public ResponseEntity<Object> addCourse(@RequestBody Course course, Principal principal) {
		try {
			Course courseData = this.userService.addCourse(course);
			if(courseData.getCourseName() == null) {
				return ResponseEntity.ok(Constants.COURSE_ALREADY_ADDED);
			}else {
				return ResponseEntity.ok(this.userService.addCourse(course));
			}
		} catch (Exception e) {
			return ResponseEntity.ok(new Exception(Constants.COURSE_ALREADY_ADDED,e));
		}
	}

	@PostMapping("/add-topics")
	public ResponseEntity<Topics> addTopics(@RequestBody Topics topics, Principal principal) {
		UserData user = this.userRepo.getUserByUserName(principal.getName());
		for (RoleModel role : user.getRoles()) {
			if (role.getRoleName().equalsIgnoreCase(Constants.ROLE_AUTHOR)
					|| role.getRoleName().equalsIgnoreCase(Constants.ROLE_MENTOR)) {
				return ResponseEntity.ok(this.userService.addTopics(topics));
			}
		}
		throw new UsernameNotFoundException(Constants.USER_NOT_FOUND_WITH_USERNAME + user.getUserName());
	}

	@PostMapping("/add-sub_topics")
	public ResponseEntity<Object> addSubTopics(@RequestBody SubTopic subTopic, Principal principal) {
		UserData user = null;
		try {
			user = this.userRepo.getUserByUserName(principal.getName());
			for (RoleModel role : user.getRoles()) {
				if (role.getRoleName().equalsIgnoreCase(Constants.ROLE_AUTHOR)
						|| role.getRoleName().equalsIgnoreCase(Constants.ROLE_MENTOR)) {
					return ResponseEntity.ok(this.userService.addSubTopics(subTopic));
				}
			}
			return ResponseEntity.ok(Constants.ADMIN_CANNOT_ADD_TOPICS);
			
		} catch (Exception e) {
			throw new UsernameNotFoundException(Constants.USER_NOT_FOUND_WITH_USERNAME + user.getUserName());
		}	
	}

	@PostMapping("/add-questions")
	public ResponseEntity<Object> addQuestions(@RequestBody Questions questions, Principal principal) {
		UserData user = null;
		try {
			user = this.userRepo.getUserByUserName(principal.getName());
			for (RoleModel role : user.getRoles()) {
				if (role.getRoleName().equalsIgnoreCase(Constants.ROLE_AUTHOR)
						|| role.getRoleName().equalsIgnoreCase(Constants.ROLE_MENTOR)) {
					return ResponseEntity.ok(this.userService.addQuestions(questions));
				}
			}
			return ResponseEntity.ok(Constants.ADMIN_CANNOT_ADD_TOPICS);
		} catch (Exception e) {
			throw new UsernameNotFoundException(Constants.USER_NOT_FOUND_WITH_USERNAME + user.getUserName());
		}
	}

	@GetMapping("/authenticate/getCourseNames")
	public ResponseEntity<List<Course>> getCourseNames() {
		return ResponseEntity.ok(this.userService.getCourseNames());
	}

	@GetMapping("/getTopics")
	public ResponseEntity<List<Topics>> getTopics() {
		return ResponseEntity.ok(this.userService.getTopics());
	}

	@GetMapping("/getSubTopics")
	public ResponseEntity<List<SubTopic>> getSubTopics() {
		return ResponseEntity.ok(this.userService.getSubTopics());
	}

	@GetMapping("/getQuestions")
	public ResponseEntity<List<Questions>> getQuestions() {
		return ResponseEntity.ok(this.userService.getQuestions());
	}

	@GetMapping("/getTopicsById/{courseId}")
	public List<Topics> getTopicByCourseId(@PathVariable("courseId") Long courseId) {
		return this.userService.getTopicByCourseId(courseId);
	}

	@GetMapping("/getSubTopicsById/{topicId}")
	public ResponseEntity<List<SubTopic>> getSubTopicByTopicId(@PathVariable("topicId") Long topicId) {
		return ResponseEntity.ok(this.userService.getSubTopicByTopicId(topicId));
	}

	@GetMapping("/getQuestionsById/{subTopicId}")
	public ResponseEntity<List<Questions>> getQuestionsBySubTopicId(@PathVariable("subTopicId") Long subTopicId) {
		return ResponseEntity.ok(this.userService.getQuestionsBySubTopicId(subTopicId));
	}
	
	@PostMapping("/sendEmail")
	public ResponseEntity<Object> sendEmail(@RequestBody EmailPayload emailPayload) {
		try {
			this.userService.sendEmail(emailPayload);
			return ResponseEntity.ok(Constants.MAIL_SEND_MESSAGE);
		} catch (MailException mailException) {
			return ResponseEntity.ok(mailException);
		}
	}
	
	@PostMapping("/sendAttachedEmail")
	public ResponseEntity<Object> sendAttachmentEmail(@RequestBody EmailPayload emailPayload) {
		try {
			this.userService.sendAttachmentEmail(emailPayload);
			return ResponseEntity.ok(Constants.MAIL_SEND_MESSAGE);
		} catch (MailException mailException) {
			return ResponseEntity.ok(mailException);
		}
	}
	
	@PostMapping("/authenticate/send-otp")
	public ResponseEntity<Object> sendOtp(@RequestBody OtpPayload otpPayload) {
		try {
			return ResponseEntity.ok(this.userService.sendOtp(otpPayload));
		} catch (Exception e) {
			return ResponseEntity.ok(new Exception("Something went wrong", e));
		}
	}
	
	@PostMapping("/authenticate/forgotPassword")
	public ResponseEntity<Object> changePassword(@RequestBody ForgotPasswordPayload forgotPasswordPayload){
		try {
			return ResponseEntity.ok(this.userService.changePassword(forgotPasswordPayload));
		} catch (Exception e) {
			return ResponseEntity.ok(new Exception("Something went wrong", e));
		}
	}

	/*
	 *  
	 	******************** LOGIN API  **************************
	 *  
	*/

	@PostMapping("/authenticate")
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody LoginPayload loginPayload) throws Exception {
		UserData user = null;
		try {
			user = this.userRepo.getUserByUserName(loginPayload.getUserName());
			if (passwordEncoder.matches(loginPayload.getPassword(), user.getPassword())) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(loginPayload.getUserName());
				String token = jwtUtil.generateToken(userDetails);
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginPayload.getUserName(),
						loginPayload.getPassword()));
				return ResponseEntity.ok(new AuthenticationResponse(token));
			} else {
				return ResponseEntity.ok(Constants.PASSWORD_INVALID);
			}

		} catch (DisabledException e) {
			throw new Exception("User Disabled", e);
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials", e);
		} catch (NullPointerException e) {
			return ResponseEntity.ok(Constants.USER_INVALID);
		}
	}
}
