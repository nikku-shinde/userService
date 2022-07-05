package com.example.usermicroservice;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.example.usermicroservice.dto.RoleDTO;
import com.example.usermicroservice.dto.UserDTO;
import com.example.usermicroservice.entity.RoleModel;
import com.example.usermicroservice.entity.UserData;
import com.example.usermicroservice.helper.Course;
import com.example.usermicroservice.helper.Questions;
import com.example.usermicroservice.helper.SubTopic;
import com.example.usermicroservice.helper.Topics;

public class UserDummyData {

	public static UserData getUser() {
		List<RoleModel> roleList = new ArrayList<RoleModel>();
		roleList.add(new RoleModel(1l,"ROLE_ADMIN"));
		UserData user = new UserData(1l, "abc" ,"abc@gmail.com","abc@123","abc@123","JAVA" ,new Date(2022-06-27), new Date(2022-06-27), roleList);
		return user;
	}
	
	public static UserDTO getUserDTO() {
		List<RoleModel> roleList = new ArrayList<RoleModel>();
		roleList.add(new RoleModel(1l,"ROLE_ADMIN"));
		UserDTO userDTO = new UserDTO("abc" ,"abc@gmail.com","abc@123","abc@123","JAVA" ,roleList);
		return userDTO;
	}
	
	public static List<UserData> getUserList() {
		List<RoleModel> roleList = new ArrayList<RoleModel>();
		roleList.add(new RoleModel(1l,"ROLE_ADMIN"));
		List<UserData> userList = new ArrayList<UserData>();
		userList.add(new UserData(1l, "abc" ,"abc@gmail.com","abc@123","abc@123","JAVA" ,new Date(2022-06-27), new Date(2022-06-27), roleList));
		userList.add(new UserData(2l, "pqr" ,"pqr@gmail.com","pqr@123","pqr@123","PYTHON" ,new Date(2022-06-27), new Date(2022-06-27),  roleList));
		return userList;
	}
	
	public static RoleModel getRole() {
		RoleModel role = new RoleModel(1l,"ROLE_ADMIN");
		return role;
	}
	
	public static RoleDTO getRoleDTO() {
		RoleDTO roleDTO = new RoleDTO("ROLE_ADMIN");
		return roleDTO;
	}
	
	public static Course getCourse() {
		Course course = new Course(1l, "Java", 1l, 2l);
		return course;
	}
	
	public static Topics getTopic() {
		Topics topic = new Topics(1l, "Java Core", getCourse());
		return topic;
	}
	
	public static SubTopic getSubTopic() {
		SubTopic subTopic = new SubTopic(1l, "Java Array", getTopic());
		return subTopic;
	}
	
	public static Questions getQuestion() {
		Questions question = new Questions(1l, "Reverse Array", getSubTopic());
		return question;
	}
	
	public static List<Course> getCourseList() {
		List<Course> courseList = new ArrayList<Course>();
		courseList.add(new Course(1l, "Java", 1l, 2l));
		courseList.add(new Course(2l, "Python", 3l, 4l));
		return courseList;
	}
	
	public static List<Topics> getTopicList() {
		List<Topics> topicList = new ArrayList<Topics>();
		topicList.add(new Topics(1l, "Java Core", getCourse()));
		topicList.add(new Topics(2l, "Java Advance", getCourse()));
		return topicList;
	}
	
	public static List<SubTopic> getSubTopicList() {
		List<SubTopic> subTopicList = new ArrayList<SubTopic>();
		subTopicList.add(new SubTopic(1l, "Java Array", getTopic()));
		subTopicList.add(new SubTopic(2l, "Java String", getTopic()));
		return subTopicList;
	}
	
	public static List<Questions> getQuestionList() {
		List<Questions> questionList = new ArrayList<Questions>();
		questionList.add(new Questions(1l, "Reverse Array", getSubTopic()));
		questionList.add(new Questions(2l, "Sort Array", getSubTopic()));
		return questionList;
	}
}
