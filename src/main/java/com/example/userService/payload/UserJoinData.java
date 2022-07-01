package com.example.userService.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserJoinData {
	
	private String name;
	private Long user_id;
	private String role_name;
}
