package com.twad.service;

import java.util.List;
import com.twad.bean.*;

import com.twad.entity.UserModel;


public interface UserService {
	
	
	public UserModel findByUserName(String userName);

	public List<UserModel> findAll();

	public boolean authenticateUser(UserBean userbean);

	public UserModel findByUserNameAndPassword(String userName, String oldPassword);


}
