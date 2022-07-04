package com.example.usermicroservice.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Questions {

	private Long id;
	private String question;
	private SubTopic subTopic;
}
