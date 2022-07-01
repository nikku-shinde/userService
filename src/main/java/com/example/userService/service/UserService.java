package com.example.userService.service;

import java.io.File;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
import com.example.userService.payload.LoginPayload;
import com.example.userService.payload.OtpPayload;

public interface UserService {
	
	public List<UserData> getAllUsers();
	
	public UserData getUserByUserName(String userName);
	
	public UserData getUserById(Long id);
	
	public List<RoleModel> getAllRoles();
	
	public UserData addUserData(UserDTO user);
	
	public void deleteUser(Long id);
	
	public List<String> getUser();
	
	public UserData updateUser(Long id , UserDTO user);
	
	public UserData addAdminUserData(UserData user);
	
	public RoleModel addRoles(RoleDTO roles);
	
	public Course addCourse(Course course);
	
	public Topics addTopics(Topics topics);
	
	public SubTopic addSubTopics(SubTopic subTopic);
	
	public Questions addQuestions(Questions questions);
	
	public List<Course> getCourseNames();
	
	public List<Topics> getTopics();
	
	public List<SubTopic> getSubTopics();
	
	public List<Questions> getQuestions();
	
	public List<Topics> getTopicByCourseId(Long course_id);
	
	public List<SubTopic> getSubTopicByTopicId(Long topic_id);
	
	public List<Questions> getQuestionsBySubTopicId(Long sub_topic_id);
	
	public void sendEmail(EmailPayload emailPayload);
	
	public void sendAttachmentEmail(EmailPayload emailPayload);
	
	public Integer sendOtp(OtpPayload otpPayload);
	
	public String changePassword(ForgotPasswordPayload forgotPasswordPayload);
}
