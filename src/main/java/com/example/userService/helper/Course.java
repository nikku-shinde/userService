package com.example.userService.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Course {

	private Long course_Id;
	private String courseName;
	private Long author_id;
	private Long mentor_id;
}
