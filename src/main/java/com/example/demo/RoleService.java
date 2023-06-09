package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.auth.Role;
import com.example.demo.auth.RoleRepo;

@Service
public class RoleService {
	@Autowired
	private RoleRepo roleRepo;
	public List <Role> findAll(){
		return roleRepo.findAll();
	}
	public Optional<Role>findById(int id){
		return roleRepo.findById(id);
	}
	public Role save (Role role) {
		return roleRepo.save(role);
	}
	public Role findByName(String name) {
        return roleRepo.findByName(name);
    }

}
