package com.example.userService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.example.userService.DTO.RoleDTO;
import com.example.userService.DTO.UserDTO;
import com.example.userService.entity.RoleModel;
import com.example.userService.entity.UserData;
import com.example.userService.helper.Course;
import com.example.userService.helper.Questions;
import com.example.userService.helper.SubTopic;
import com.example.userService.helper.Topics;
import com.example.userService.repository.RoleRepository;
import com.example.userService.repository.UserRepository;
import com.example.userService.service.UserServiceImpl;
import com.example.userService.util.CourseApiUrl;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = {UserServiceImplTest.class})
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepo;
	
	@Mock
	private RoleRepository roleRepo;
	
	@InjectMocks
	private UserServiceImpl service;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private JavaMailSender javaMailSender;
	
	@Test
	@Order(1)
	public void test_getAllUsers() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_ADMIN"));
		List<UserData> user_list = new ArrayList<UserData>();
		user_list.add(new UserData(1l, "abc" ,"abc@gmail.com","abc@123","abc@123","JAVA" ,new Date(2022-06-27), new Date(2022-06-27), role_list));
		user_list.add(new UserData(2l, "pqr" ,"pqr@gmail.com","pqr@123","pqr@123","PYTHON" ,new Date(2022-06-27), new Date(2022-06-27),  role_list));
		when(userRepo.findAll()).thenReturn(user_list); // mocking
		assertEquals(2, service.getAllUsers().size());
	}
	
	@Test
	@Order(2)
	public void test_getUserById() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_ADMIN"));
		UserData user = new UserData(1l, "abc" ,"abc@gmail.com","abc@123","abc@12344","JAVA" , new Date(2022-06-27), new Date(2022-06-27) , role_list);
		Long user_id = 1l;
		when(userRepo.getUserById(user_id)).thenReturn(user); // mocking
		assertEquals(user_id, service.getUserById(user_id).getId());
	}
	
	@Test
	@Order(3)
	public void test_getUserByUserName() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_ADMIN"));
		UserData user = new UserData(1l, "abc" ,"abc@gmail.com","abc@123","abc@12344","JAVA" , new Date(2022-06-27), new Date(2022-06-27), role_list);
		String user_name = "abc@123";
		when(userRepo.getUserByUserName(user_name)).thenReturn(user);
		assertEquals(user_name, service.getUserByUserName(user_name).getUserName());
	}
	
	@Test
	@Order(4)
	public void test_addUserData() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_AUTHOR"));
		role_list.add(new RoleModel(2l,"ROLE_MENTOR"));
		when(roleRepo.findAll()).thenReturn(role_list);
		UserDTO userDTO = new UserDTO("abc" ,"abc@gmail.com","abc@123","abc@12344","JAVA",role_list);
		UserData user = new UserData(1l, userDTO.getName() ,userDTO.getEmail(),userDTO.getUserName(),userDTO.getPassword(),userDTO.getProfile() , new Date(2022-06-27), new Date(2022-06-27), userDTO.getRoles());
		when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
		when(userRepo.save(user)).thenReturn(user);
		service.addUserData(userDTO);
	}
	
	@Test
	@Order(5)
	public void test_addRoles() {
		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setRoleName("ROLE_AUTHOR");
		RoleModel role = new RoleModel(1l,roleDTO.getRoleName());
		when(roleRepo.save(role)).thenReturn(role);
		service.addRoles(roleDTO);
	}
	
	@Test
	@Order(6)
	public void test_getAllRoles() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_AUTHOR"));
		role_list.add(new RoleModel(2l,"ROLE_MENTOR"));
		when(roleRepo.findAll()).thenReturn(role_list);
		assertEquals(2, service.getAllRoles().size());
	}
	
	@Test
	@Order(7)
	public void test_updateUser() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_AUTHOR"));
		role_list.add(new RoleModel(2l,"ROLE_MENTOR"));
		UserDTO userDTO = new UserDTO("abc" ,"abc@gmail.com","abc@123","abc@12344","JAVA",role_list);
		UserData user = new UserData(1l, userDTO.getName() ,userDTO.getEmail(),userDTO.getUserName(),userDTO.getPassword(),userDTO.getProfile() , new Date(2022-06-27), new Date(2022-06-27), userDTO.getRoles());
		Long user_id = 1l;
		when(userRepo.getUserById(user_id)).thenReturn(user);
		when(userRepo.save(user)).thenReturn(user);
		service.updateUser(user_id, userDTO);
	}
	
	@Test
	@Order(8)
	public void test_deleteUser() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_AUTHOR"));
		role_list.add(new RoleModel(2l,"ROLE_MENTOR"));
		UserData user = new UserData(1l, "abc" ,"abc@gmail.com","abc@123","abc@12344","JAVA" , new Date(2022-06-27), new Date(2022-06-27), role_list);
		Long user_id = 1l;
		when(userRepo.getUserById(user_id)).thenReturn(user);
		service.deleteUser(user_id);
		verify(userRepo,times(1)).delete(user);
	}
	
	@Test
	@Order(9)
	public void test_getUser() {
		List<String> users = new ArrayList<String>();
		users.add("abc");
		when(userRepo.getUserData()).thenReturn(users);
		assertEquals(1, service.getUser().size());
		
	}
	
	@Test
	@Order(10)
	public void test_addCourse() {
		Course course = new Course(1l, "JAVA", 2l, 3l);
		when(restTemplate.postForObject(CourseApiUrl.ADD_COURSE_API_ENDPOINT, course, Course.class)).thenReturn(course);
		course.setCourseId(course.getCourseId());
		course.setCourseName(course.getCourseName());
		course.setAuthorId(course.getAuthorId());
		course.setMentorId(course.getMentorId());
		assertEquals(course, service.addCourse(course));
	}
	
	@Test
	@Order(11)
	public void test_addTopics() {
		Course course = new Course(1l, "JAVA", 2l, 3l);
		Topics topics = new Topics(1l, "Java Core", course);
		when(restTemplate.postForObject(CourseApiUrl.ADD_TOPICS_API_ENDPOINT, topics, Topics.class)).thenReturn(topics);
		topics.setId(topics.getId());
		topics.setTopicName(topics.getTopicName());
		topics.setCourse(topics.getCourse());
		assertEquals(topics, service.addTopics(topics));
	}
	
	@Test
	@Order(12)
	public void test_addSubTopics() {
		Course course = new Course(1l, "JAVA", 2l, 3l);
		Topics topics = new Topics(1l, "Java Core", course);
		SubTopic subTopic = new SubTopic(1l, "Java Introduction", topics);
		when(restTemplate.postForObject(CourseApiUrl.ADD_SUB_TOPICS_API_ENDPOINT, subTopic, SubTopic.class)).thenReturn(subTopic);
		subTopic.setId(subTopic.getId());
		subTopic.setSubTopicName(subTopic.getSubTopicName());
		subTopic.setTopic(subTopic.getTopic());
		assertEquals(subTopic, service.addSubTopics(subTopic));
	}
	
	@Test
	@Order(13)
	public void test_addQuestions() {
		Course course = new Course(1l, "JAVA", 2l, 3l);
		Topics topics = new Topics(1l, "Java Core", course);
		SubTopic subTopic = new SubTopic(1l, "Java Introduction", topics);
		Questions questions = new Questions(1l, "What is class", subTopic);
		when(restTemplate.postForObject(CourseApiUrl.ADD_QUESTIONS_API_ENDPOINT, questions, Questions.class)).thenReturn(questions);
		questions.setId(questions.getId());
		questions.setQuestion(questions.getQuestion());
		questions.setSubTopic(questions.getSubTopic());
		assertEquals(questions, service.addQuestions(questions));
	}
	
	@Test
	@Order(14)
	public void test_getCourseNames() {
		ResponseEntity<List<Course>> claimResponse = restTemplate.exchange(
				CourseApiUrl.COURSES_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Course>>() {
				});
		when(restTemplate.exchange(
				CourseApiUrl.COURSES_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Course>>() {
				})).thenReturn(claimResponse);
		if (claimResponse != null && claimResponse.hasBody()) {
			assertEquals(claimResponse.getBody().size(), service.getCourseNames().size());
		}
	}
	
	@Test
	@Order(15)
	public void test_getTopics() {
		ResponseEntity<List<Topics>> claimResponse = restTemplate.exchange(
				CourseApiUrl.TOPICS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Topics>>() {
				});
		when(restTemplate.exchange(
				CourseApiUrl.TOPICS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Topics>>() {
				})).thenReturn(claimResponse);
		if (claimResponse != null && claimResponse.hasBody()) {
			assertEquals(claimResponse.getBody().size(), service.getTopics().size());
		}
	}
	
	@Test
	@Order(16)
	public void test_getSubTopics() {
		ResponseEntity<List<SubTopic>> claimResponse = restTemplate.exchange(
				CourseApiUrl.SUB_TOPICS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SubTopic>>() {
				});
		when(restTemplate.exchange(
				CourseApiUrl.SUB_TOPICS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SubTopic>>() {
				})).thenReturn(claimResponse);
		if (claimResponse != null && claimResponse.hasBody()) {
			assertEquals(claimResponse.getBody().size(), service.getSubTopics().size());
		}
	}
	
	@Test
	@Order(17)
	public void test_getQuestions() {
		ResponseEntity<List<Questions>> claimResponse = restTemplate.exchange(
				CourseApiUrl.QUESTIONS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Questions>>() {
				});
		when(restTemplate.exchange(
				CourseApiUrl.QUESTIONS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Questions>>() {
				})).thenReturn(claimResponse);
		if (claimResponse != null && claimResponse.hasBody()) {
			assertEquals(claimResponse.getBody().size(), service.getQuestions().size());
		}
	}
	
	@Test
	@Order(18)
	public void test_getTopicByCourseId() {
		Long course_id = 1l;
		ResponseEntity<List<Topics>> claimResponse = restTemplate.exchange(
				String.format(CourseApiUrl.TOPICS_BY_COURSE_ID_API_ENDPOINT, course_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Topics>>() {
				});
		when(restTemplate.exchange(
				String.format(CourseApiUrl.TOPICS_BY_COURSE_ID_API_ENDPOINT, course_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Topics>>() {
				})).thenReturn(claimResponse);
		if (claimResponse != null && claimResponse.hasBody()) {
			assertEquals(claimResponse.getBody().size(), service.getTopicByCourseId(course_id).size());
		}
	}
	
	@Test
	@Order(19)
	public void test_getSubTopicByTopicId() {
		Long topic_id = 1l;
		ResponseEntity<List<SubTopic>> claimResponse = restTemplate.exchange(
				String.format(CourseApiUrl.SUB_TOPICS_BY_TOPIC_ID_API_ENDPOINT, topic_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SubTopic>>() {
				});
		when(restTemplate.exchange(
				String.format(CourseApiUrl.SUB_TOPICS_BY_TOPIC_ID_API_ENDPOINT, topic_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SubTopic>>() {
				})).thenReturn(claimResponse);
		if (claimResponse != null && claimResponse.hasBody()) {
			assertEquals(claimResponse.getBody().size(), service.getSubTopicByTopicId(topic_id).size());
		}
	}
	
	@Test
	@Order(20)
	public void test_getQuestionsBySubTopicId() {
		Long sub_topic_id = 1l;
		ResponseEntity<List<Questions>> claimResponse = restTemplate.exchange(
				String.format(CourseApiUrl.QUESTIONS_BY_SUB_TOPIC_ID_API_ENDPOINT, sub_topic_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Questions>>() {
				});
		when(restTemplate.exchange(
				String.format(CourseApiUrl.QUESTIONS_BY_SUB_TOPIC_ID_API_ENDPOINT, sub_topic_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Questions>>() {
				})).thenReturn(claimResponse);
		if (claimResponse != null && claimResponse.hasBody()) {
			assertEquals(claimResponse.getBody().size(), service.getQuestionsBySubTopicId(sub_topic_id).size());
		}
	}
}
