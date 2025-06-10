package com.twad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twad.entity.UserModel;
import com.twad.repo.UserRepository;

import com.twad.bean.*;

@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserModel findByUserName(String userName, String application) {
		return userRepository.findByUserName(userName, application);		
	}

	@Override
	public List<UserModel> findAll() {
		return userRepository.findAll();
	}


	@Override
	public boolean authenticateUser(UserBean userbean, String application) {
	    // Use the correct repository method
	    UserModel user = userRepository.findByUserName(
	        userbean.getUserName(), application);

	    // Null check and match password
	    if (user != null &&
	        userbean.getUserPassword().equals(user.getUserPassword())) {
	        return true;
	    }

	    return false;
	}


	@Override
	public UserModel findByUserNameAndPassword(String userName, String oldPassword) {
		
		UserModel user = userRepository.findByUserByNamePassword(userName,oldPassword);		
		return user;
	}
	

	


}
