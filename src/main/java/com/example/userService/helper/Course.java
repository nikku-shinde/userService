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

	private Long courseId;
	private String courseName;
	private Long authorId;
	private Long mentorId;
}
