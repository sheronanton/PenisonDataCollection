package com.twad.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.twad.entity.UserModel;
import com.twad.repo.UserRepository;
import com.twad.service.UserService;

import com.twad.bean.*;


@RestController
@RequestMapping("employees")
//@RequestMapping("twad_data_collection/employees")

public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/user")
	public List<UserModel> getAllUser() {
		return userService.findAll();
	}

	@PostMapping("/user")
	public UserModel getUserByUserName(@RequestBody UserBean userbean) {
		return userService.findByUserName(userbean.getUserName(), "pension");
	}

	@PostMapping("/user/authenticateUser")
	public ResponseEntity<?> authenticateUser(@RequestBody UserBean userbean) {
		boolean userExists = userService.authenticateUser(userbean,  "pension");

		UserBean userBean = null;
		ResponseBean resp = new ResponseBean();
		UserModel user = userService.findByUserName(userbean.getUserName(),  "pension");
		ModelMapper modelMapper = new ModelMapper();
		if (userExists) {
			System.out.println("User available");
			userBean = modelMapper.map(user, UserBean.class);
			resp.setUser(userBean);
			resp.setIsError(0);
			resp.setMessage("success");
			return ResponseEntity.ok(resp);
		} else {
			System.out.println("User not available");
			resp.setIsError(1);
			resp.setMessage("Invalid user");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		}
	}

	@PostMapping("/user/initailResetPassword")
	public ResponseEntity<?> updateIniPass(@RequestBody UserBean userbean) {
		UserBean userBean = null;
		ResponseBean resp = new ResponseBean();
		UserModel user = userService.findByUserNameAndPassword(userbean.getUserName(), userbean.getOldPassword());
		if (user != null) {
			if (user.getIsPasswordChanged() != (int) 1) {
				user.setUserPassword(userbean.getUserPassword());
				user.setIsPasswordChanged((int) 1);
				userRepository.save(user);
				ModelMapper modelMapper = new ModelMapper();
				System.out.println("User available");
				userBean = modelMapper.map(user, UserBean.class);
				resp.setUser(userBean);
				resp.setIsError(0);
				resp.setMessage("success");
				return ResponseEntity.ok(resp);
			} else {
				System.out.println("User not available");
				resp.setIsError(1);
				resp.setMessage("Invalid user");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
			}
		} else {
			System.out.println("User not available");
			resp.setIsError(1);
			resp.setMessage("Old Password Missmatched");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		}
	}

}
