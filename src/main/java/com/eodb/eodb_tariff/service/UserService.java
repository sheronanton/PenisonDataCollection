package com.eodb.eodb_tariff.service;

import java.util.List;

import com.eodb.eodb_tariff.bean.UserBean;
import com.eodb.eodb_tariff.entity.UserModel;


public interface UserService {
	
	
	public UserModel findByUserName(String userName);

	public List<UserModel> findAll();

	public boolean authenticateUser(UserBean userbean);

	public UserModel findByUserNameAndPassword(String userName, String oldPassword);


}
