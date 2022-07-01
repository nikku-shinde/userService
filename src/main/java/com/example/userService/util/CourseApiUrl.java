package com.example.userService.util;

public class CourseApiUrl {
	
	/*
	*
		********** COURSE-SERVICE API *************
	*
	*/
	

	public static final String ADD_COURSE_API_ENDPOINT = "http://COURSE-SERVICE//courses/add-course";
	public static final String ADD_TOPICS_API_ENDPOINT = "http://COURSE-SERVICE//courses/add-topics";
	public static final String ADD_SUB_TOPICS_API_ENDPOINT = "http://COURSE-SERVICE//courses/add-sub_topics";
	public static final String ADD_QUESTIONS_API_ENDPOINT = "http://COURSE-SERVICE//courses/add-questions";
	public static final String COURSES_LIST_API_ENDPOINT = "http://COURSE-SERVICE//courses/getCourseNames";
	public static final String TOPICS_LIST_API_ENDPOINT = "http://COURSE-SERVICE//courses/getTopics";
	public static final String SUB_TOPICS_LIST_API_ENDPOINT = "http://COURSE-SERVICE//courses/getSubTopics";
	public static final String QUESTIONS_LIST_API_ENDPOINT = "http://COURSE-SERVICE//courses/getQuestions";
	public static final String TOPICS_BY_COURSE_ID_API_ENDPOINT = "http://COURSE-SERVICE//courses/getTopicsById/%d";
	public static final String SUB_TOPICS_BY_TOPIC_ID_API_ENDPOINT = "http://COURSE-SERVICE//courses/getSubTopicsById/%d";
	public static final String QUESTIONS_BY_SUB_TOPIC_ID_API_ENDPOINT = "http://COURSE-SERVICE//courses/getQuestionsById/%d";
}
