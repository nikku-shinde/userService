package com.example.usermicroservice.util;

public class Constants {

	private Constants() {
		throw new IllegalStateException("Constants utility class");
	}
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_AUTHOR = "ROLE_AUTHOR";
	public static final String ROLE_MENTOR = "ROLE_MENTOR";
	public static final String ROLE_TRAINEE = "ROLE_TRAINEE";
	public static final String USER_NAME = "userName";
	public static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username: ";
	public static final String USER_DELETED_SUCCESSFULLY = "User Deleted Successfully";
	public static final String COURSE_ALREADY_ADDED = "Course Already Added";
	public static final String ADMIN_CANNOT_ADD_TOPICS = "Admin cannot add topics";
	public static final String ADMIN_CANNOT_ADD_SUBTOPICS = "Admin cannot add sub topics";
	public static final String ADMIN_CANNOT_ADD_QUESTIONS = "Admin cannot add sub Questions";
	public static final String MAIL_SEND_MESSAGE = "Congratulations! Your mail has been send to the user.";
	public static final String PASSWORD_INVALID = "Password Invalid";
	public static final String USER_INVALID = "User Invalid";
	public static final String USER_ADDED_SUCCESSFULLY = "User Added Successfully";
	public static final String ROLE_ADDED_SUCCESSFULLY = "Role Added Successfully";
	public static final String USER_UPDATED_SUCCESSFULLY = "User Updated Successfully";
	public static final String USER_CANNOT_DELETE = "User cannot delete";
	public static final String USER_CANNOT_UPDATE = "User cannot update";
	public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password Changed Successfully";
}
