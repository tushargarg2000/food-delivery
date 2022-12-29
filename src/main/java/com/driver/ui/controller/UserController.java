package com.driver.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.driver.model.request.UserDetailsRequestModel;
import com.driver.model.response.OperationStatusModel;
import com.driver.model.response.RequestOperationName;
import com.driver.model.response.RequestOperationStatus;
import com.driver.model.response.UserResponse;
import com.driver.shared.dto.UserDto;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driver.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	
	/**
	 * Initialize Logger object
	 */
	org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping(path = "/{id}")
	public UserResponse getUser(@PathVariable String id) throws Exception{

		logger.info("UserController -> getUser(id) method has been called");
		UserResponse returnValue = new UserResponse();

		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);

		logger.info("Return the user to the requester");
		return returnValue;
	}

	@PostMapping()
	public UserResponse createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{

		logger.info("UserController -> createUser(userDetails) method has been called");
		UserResponse returnValue = new UserResponse();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);

		logger.info("Return the user to the requester");
		return returnValue;
	}

	@PutMapping(path = "/{id}")
	public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) throws Exception{
		
		logger.info("UserController -> updateUser(id, userDetails) method has been called");
		UserResponse returnValue = new UserResponse();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);
		
		logger.info("Return the user to the requester");
		return returnValue;
	}

	@DeleteMapping(path = "/{id}")
	public OperationStatusModel deleteUser(@PathVariable String id) throws Exception{
		
		logger.info("UserController -> deleteUser(id) method has been called");
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		logger.info("Return the message to the requester");
		return returnValue;
	}
	
	@GetMapping()
	public List<UserResponse> getUsers(){
		
		logger.info("UserController -> getUsers() method has been called");
		List<UserResponse> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers();
		
		for(UserDto userDto : users) {
			UserResponse userModel = new UserResponse();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		logger.info("Return the users' list to the requester");
		return returnValue;
	}
	
}
