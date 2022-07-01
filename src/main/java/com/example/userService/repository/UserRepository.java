package com.example.userService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.userService.entity.UserData;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long>{

	@Query("select u from UserData u where u.id = :id")
	public UserData getUserById(@Param("id") Long id);
	
	@Query("select a from UserData a where a.userName=:userName")
	public UserData getUserByUserName(@Param("userName") String userName);
	
	@Query(value = "select user_id from users_roles_tab where role_id=id" , nativeQuery = true)
	public List<Long> getUsersByRoleId(@Param("id") Long id);
	
	@Query("select u from UserData u where u.name=:name")
	public UserData getUserByName(@Param("name") String name);
	
	
	@Query(value = "select role_id from role_tab where role_name='ROLE_AUTHOR'" , nativeQuery = true)
	public Long getRoleIdByRoleName();
	
	
	@Query(value = "select user_tab.name , user_tab.user_id , role_tab.role_name , user_tab.profile from user_tab join users_roles_tab on user_tab.user_id = users_roles_tab.user_id join role_tab on role_tab.role_id = users_roles_tab.role_id;" , nativeQuery = true)
	public List<String> getUserData();
	
	@Query(value = "select user_id from users_roles_tab where role_id = ?1" , nativeQuery = true)
	public List<Long> getUserIdByRoleId(@Param("role_id") Long role_id);
	
	@Query("select e from UserData e where e.email=:email")
	public UserData getUserByEmail(@Param("email") String email);
}
