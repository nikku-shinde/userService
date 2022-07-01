package com.example.userService.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.userService.entity.RoleModel;
import com.example.userService.entity.UserData;
import com.example.userService.repository.RoleRepository;
import com.example.userService.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserRepository userDataRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

		UserData user = this.userDataRepo.getUserByUserName(username);

		List<RoleModel> roleList = this.roleRepo.findAll();

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		} else {
			for (RoleModel roleData : roleList) {
				for (RoleModel role_data : user.getRoles()) {
					if (roleData.getId().equals(role_data.getId())) {

						if (role_data.getRoleName().equalsIgnoreCase("ROLE_ADMIN")) {
							roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
						} else if (role_data.getRoleName().equalsIgnoreCase("ROLE_AUTHOR")) {
							roles.add(new SimpleGrantedAuthority("ROLE_AUTHOR"));
						} else if (role_data.getRoleName().equalsIgnoreCase("ROLE_MENTOR")) {
							roles.add(new SimpleGrantedAuthority("ROLE_MENTOR"));
						} else if (role_data.getRoleName().equalsIgnoreCase("ROLE_TRAINEE")) {
							roles.add(new SimpleGrantedAuthority("ROLE_TRAINEE"));
						}
					}
				}
			}
			return new User(user.getUserName(), user.getPassword(), roles);
		}
	}

}
