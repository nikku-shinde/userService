package com.example.usermicroservice.config;

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

import com.example.usermicroservice.entity.RoleModel;
import com.example.usermicroservice.entity.UserData;
import com.example.usermicroservice.repository.RoleRepository;
import com.example.usermicroservice.repository.UserRepository;
import com.example.usermicroservice.util.Constants;

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
			throw new UsernameNotFoundException(Constants.USER_NOT_FOUND_WITH_USERNAME + username);
		} else {
			for (RoleModel roleData : roleList) {
				for (RoleModel role_data : user.getRoles()) {
					if (roleData.getId().equals(role_data.getId())) {

						if (role_data.getRoleName().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
							roles.add(new SimpleGrantedAuthority(Constants.ROLE_ADMIN));
						} else if (role_data.getRoleName().equalsIgnoreCase(Constants.ROLE_AUTHOR)) {
							roles.add(new SimpleGrantedAuthority(Constants.ROLE_AUTHOR));
						} else if (role_data.getRoleName().equalsIgnoreCase(Constants.ROLE_MENTOR)) {
							roles.add(new SimpleGrantedAuthority(Constants.ROLE_MENTOR));
						} else if (role_data.getRoleName().equalsIgnoreCase(Constants.ROLE_TRAINEE)) {
							roles.add(new SimpleGrantedAuthority(Constants.ROLE_TRAINEE));
						}
					}
				}
			}
			return new User(user.getUserName(), user.getPassword(), roles);
		}
	}

}
