package com.example.userService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.userService.DTO.RoleDTO;
import com.example.userService.DTO.UserDTO;
import com.example.userService.entity.RoleModel;
import com.example.userService.entity.UserData;
import com.example.userService.helper.Course;
import com.example.userService.helper.Questions;
import com.example.userService.helper.SubTopic;
import com.example.userService.helper.Topics;
import com.example.userService.payload.EmailPayload;
import com.example.userService.payload.ForgotPasswordPayload;
import com.example.userService.payload.OtpPayload;
import com.example.userService.repository.RoleRepository;
import com.example.userService.repository.UserRepository;
import com.example.userService.util.CourseApiUrl;

@Service
public class UserServiceImpl implements UserService {
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	Random random = new Random(1000);	
	
	@Override
	public List<UserData> getAllUsers() {
		return this.userRepo.findAll();
	}
	

	@Override
	public UserData getUserByUserName(String userName) {
		return this.userRepo.getUserByUserName(userName);
	}


	@Override
	public UserData getUserById(Long id) {
		return this.userRepo.getUserById(id);
	}

	
	@Override
	public UserData addUserData(UserDTO user) {
		UserData userData = new UserData();
		userData.setName(user.getName());
		userData.setEmail(user.getEmail());
		userData.setUserName(user.getUserName());
		userData.setProfile(user.getProfile());
		List<RoleModel> roles = roleRepo.findAll();
		List<RoleModel> user_roles = new ArrayList<>();
		for (RoleModel role_user : user.getRoles()) {
			for (RoleModel role : roles) {
				if (role.getId().equals(role_user.getId())) {
					user_roles.add(role);
					userData.setRoles(user_roles);
					userData.setPassword(passwordEncoder.encode(user.getPassword()));
				}
			}
		}
		return this.userRepo.save(userData);
	}

	@Override
	public RoleModel addRoles(RoleDTO roles) {
		RoleModel role = new RoleModel();
		role.setRole_name(roles.getRole_name());
		return this.roleRepo.save(role);
	}

	@Override
	public UserData addAdminUserData(UserData user) {
		List<RoleModel> roles = roleRepo.findAll();
		List<RoleModel> user_roles = new ArrayList<>();
		for (RoleModel role_user : user.getRoles()) {
			for (RoleModel role : roles) {
				if (role.getId().equals(role_user.getId())) {
					user_roles.add(role);
					user.setRoles(user_roles);
					user.setPassword(passwordEncoder.encode(user.getPassword()));
				}
			}
		}
		return this.userRepo.save(user);
	}

	@Override
	public Course addCourse(Course course) {
		Course courses = this.restTemplate.postForObject(CourseApiUrl.ADD_COURSE_API_ENDPOINT, course,
				Course.class);
		course.setCourse_Id(courses.getCourse_Id());
		course.setCourseName(courses.getCourseName());
		course.setAuthor_id(courses.getAuthor_id());
		course.setMentor_id(courses.getMentor_id());
		return course;
	}

	@Override
	public Topics addTopics(Topics topics) {
		Topics topics_data = this.restTemplate.postForObject(CourseApiUrl.ADD_TOPICS_API_ENDPOINT, topics,
				Topics.class);
		topics.setId(topics_data.getId());
		topics.setTopicName(topics_data.getTopicName());
		topics.setCourse(topics_data.getCourse());
		return topics;
	}

	@Override
	public SubTopic addSubTopics(SubTopic subTopic) {
		SubTopic subTopic_data = this.restTemplate.postForObject(CourseApiUrl.ADD_SUB_TOPICS_API_ENDPOINT,
				subTopic, SubTopic.class);

		subTopic.setId(subTopic_data.getId());
		subTopic.setSubTopicName(subTopic_data.getSubTopicName());
		subTopic.setTopic(subTopic_data.getTopic());
		return subTopic;
	}

	@Override
	public Questions addQuestions(Questions questions) {
		Questions question_data = this.restTemplate.postForObject(CourseApiUrl.ADD_QUESTIONS_API_ENDPOINT,
				questions, Questions.class);
		questions.setId(question_data.getId());
		questions.setQuestion(question_data.getQuestion());
		questions.setSubTopic(question_data.getSubTopic());
		return questions;
	}

	@Override
	public List<Course> getCourseNames() {
		List<Course> course = new ArrayList<Course>();
		ResponseEntity<List<Course>> claimResponse = restTemplate.exchange(
				CourseApiUrl.COURSES_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Course>>() {
				});
		if (claimResponse != null && claimResponse.hasBody()) {
			course = claimResponse.getBody();
		}
		return course;
	}

	@Override
	public List<Topics> getTopics() {
		List<Topics> topic = new ArrayList<Topics>();
		ResponseEntity<List<Topics>> claimResponse = restTemplate.exchange(
				CourseApiUrl.TOPICS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Topics>>() {
				});
		if (claimResponse != null && claimResponse.hasBody()) {
			topic = claimResponse.getBody();
		}
		return topic;
	}

	@Override
	public List<SubTopic> getSubTopics() {
		List<SubTopic> subTopic = new ArrayList<SubTopic>();
		ResponseEntity<List<SubTopic>> claimResponse = restTemplate.exchange(
				CourseApiUrl.SUB_TOPICS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SubTopic>>() {
				});
		if (claimResponse != null && claimResponse.hasBody()) {
			subTopic = claimResponse.getBody();
		}
		return subTopic;
	}

	@Override
	public List<Questions> getQuestions() {
		List<Questions> question = new ArrayList<Questions>();
		ResponseEntity<List<Questions>> claimResponse = restTemplate.exchange(
				CourseApiUrl.QUESTIONS_LIST_API_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Questions>>() {
				});
		if (claimResponse != null && claimResponse.hasBody()) {
			question = claimResponse.getBody();
		}
		return question;
	}

	@Override
	public List<Topics> getTopicByCourseId(Long course_id) {
		List<Topics> topic = new ArrayList<Topics>();
		ResponseEntity<List<Topics>> claimResponse = restTemplate.exchange(
				String.format(CourseApiUrl.TOPICS_BY_COURSE_ID_API_ENDPOINT, course_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Topics>>() {
				});
		if (claimResponse != null && claimResponse.hasBody()) {
			topic = claimResponse.getBody();
		}
		return topic;
	}

	@Override
	public List<SubTopic> getSubTopicByTopicId(Long topic_id) {
		List<SubTopic> subTopic = new ArrayList<SubTopic>();
		ResponseEntity<List<SubTopic>> claimResponse = restTemplate.exchange(
				String.format(CourseApiUrl.SUB_TOPICS_BY_TOPIC_ID_API_ENDPOINT, topic_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SubTopic>>() {
				});
		if (claimResponse != null && claimResponse.hasBody()) {
			subTopic = claimResponse.getBody();
		}
		return subTopic;
	}

	@Override
	public List<Questions> getQuestionsBySubTopicId(Long sub_topic_id) {
		List<Questions> question = new ArrayList<Questions>();
		ResponseEntity<List<Questions>> claimResponse = restTemplate.exchange(
				String.format(CourseApiUrl.QUESTIONS_BY_SUB_TOPIC_ID_API_ENDPOINT, sub_topic_id), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Questions>>() {
				});
		if (claimResponse != null && claimResponse.hasBody()) {
			question = claimResponse.getBody();
		}
		return question;
	}

	@Override
	public void deleteUser(Long id) {
		UserData user = this.userRepo.getUserById(id);
		this.userRepo.delete(user);
	}

	@Override
	public UserData updateUser(Long id, UserDTO user) {
		UserData userData = this.userRepo.getUserById(id);
		userData.setName(user.getName());
		userData.setProfile(user.getProfile());
		userData.setRoles(user.getRoles());
		return this.userRepo.save(userData);
	}

	@Override
	public void sendEmail(EmailPayload emailPayload) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(emailPayload.getEmail());
		simpleMailMessage.setSubject(emailPayload.getSubject());
		simpleMailMessage.setText(emailPayload.getText());
		
		javaMailSender.send(simpleMailMessage);
	}

	@Override
	public void sendAttachmentEmail(EmailPayload emailPayload) {
		MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(emailPayload.getEmail());
			helper.setSubject(emailPayload.getSubject());
			helper.setText(emailPayload.getText());
			FileSystemResource file = new FileSystemResource(emailPayload.getFile());
			helper.addAttachment(file.getFilename() , file);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		javaMailSender.send(mimeMessage);
	}

	@Override
	public Integer sendOtp(OtpPayload otpPayload) {
		EmailPayload mail = new EmailPayload();
		int otp = random.nextInt(999999);

		String subject = "OTP from Training And Developement";
		String message = "OTP = "+otp;
		mail.setEmail(otpPayload.getEmail());
		mail.setSubject(subject);
		mail.setText(message);
		this.sendEmail(mail);
		return otp;
	}

	@Override
	public String changePassword(ForgotPasswordPayload forgotPasswordPayload) {
		
		UserData user = this.userRepo.getUserByEmail(forgotPasswordPayload.getEmail());
		user.setPassword(passwordEncoder.encode(forgotPasswordPayload.getNewPassword()));
		this.userRepo.save(user);
		return "Password Updated Successfully";
	}


	@Override
	public List<RoleModel> getAllRoles() { 
		return this.roleRepo.findAll();
	}


	@Override
	public List<String> getUser() {
		return this.userRepo.getUserData();
	}
}
