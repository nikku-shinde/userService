package com.example.usermicroservice;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.usermicroservice.controller.UserController;
import com.example.usermicroservice.entity.RoleModel;
import com.example.usermicroservice.entity.UserData;
import com.example.usermicroservice.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class UserControllerTest {

	@Mock
	private UserService service;
	
	@InjectMocks
	private UserController userController;
	
	@Test
	@Order(1)
	public void test_getAllUsers() {
		List<RoleModel> role_list = new ArrayList<RoleModel>();
		role_list.add(new RoleModel(1l,"ROLE_ADMIN"));
		List<UserData> user_list = new ArrayList<UserData>();
		user_list.add(new UserData(1l, "abc" ,"abc@gmail.com","abc@123","abc@123","JAVA" ,new Date(2022-06-27), new Date(2022-06-27), role_list));
		user_list.add(new UserData(2l, "pqr" ,"pqr@gmail.com","pqr@123","pqr@123","PYTHON" ,new Date(2022-06-27), new Date(2022-06-27),  role_list));
		when(service.getAllUsers()).thenReturn(user_list);
		ResponseEntity<List<UserData>> res = userController.getAllUsers();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
}
