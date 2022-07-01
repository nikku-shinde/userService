package com.example.userService.payload;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailPayload {

	private String email;
	private String subject;
	private String text;
	private File file;
}
