package com.example.usermicroservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.usermicroservice.entity.RoleModel;




public interface RoleRepository extends JpaRepository<RoleModel, Long> {
	
	@Query("select a from RoleModel a where a.roleName=:roleName")
	public List<RoleModel> getRoleModelByRole(@Param("roleName") String roleName);
}

