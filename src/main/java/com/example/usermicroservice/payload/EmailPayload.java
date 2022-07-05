package com.example.usermicroservice.payload;

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
	
	public EmailPayload(String email, String subject, String text) {
		super();
		this.email = email;
		this.subject = subject;
		this.text = text;
	}
	
	
}
