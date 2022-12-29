package com.driver.service.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.driver.shared.dto.UserDto;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.io.entity.UserEntity;
import com.driver.io.repository.UserRepository;
import com.driver.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	/**
	 * Logger initialization
	 */
	org.slf4j.Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * Create user method
	 */
	@Override
	public UserDto createUser(UserDto user) throws Exception{

		logger.info("UserService -> createUser(user) method is called. Checking database for existing user details");

		if (userRepository.findByEmail(user.getEmail()) != null) {

			logger.error("Error! Record already exists.");

			throw new Exception("Record already exists!");
		}

		logger.info("Creating new user");

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String publicUserId = String.valueOf(new SecureRandom());
		userEntity.setUserId(publicUserId);

		logger.info("Saving the user to the database -> users");

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);

		logger.info("Returning the user details to the UserController via UserDto object");
		
		return returnValue;
	}


	/**
	 * Get user by email address method
	 */
	@Override
	public UserDto getUser(String email) throws Exception {

		logger.info("UserService -> getUser(email) method is called. Finding the user");

		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {

			logger.error("Can't find the user id from the database");

			throw new Exception(email);
		}

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		logger.info("User details found. Returning the user to the UserController via UserDto");

		return returnValue;
	}

	/**
	 * Get user by user id method
	 */
	@Override
	public UserDto getUserByUserId(String userId) throws Exception {

		logger.info("UserService -> getUser(userId) method is called. Finding the user");

		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {

			logger.error("Can't find the user id from the database");

			throw new Exception(userId);
		}
		BeanUtils.copyProperties(userEntity, returnValue);

		logger.info("User details found. Returning the user to the UserController via UserDto object");

		return returnValue;
	}

	/**
	 * Update user method
	 */
	@Override
	public UserDto updateUser(String userId, UserDto user) throws Exception {

		logger.info("UserService -> updateUser(userId, user) method is called. Finding the user");

		UserDto returnValue = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {

			logger.error("Can't find the user id from the database");

			throw new Exception(userId);
		}

		logger.info("User details found. Updating user details");
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		logger.info("Saving updated details to the database -> users");
		
		UserEntity updatedUserDetails = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails, returnValue);

		logger.info("Returning the user to the UserController via UserDto object");
		
		return returnValue;
	}

	/**
	 * Delete use method
	 */
	@Override
	public void deleteUser(String userId) throws Exception {

		logger.info("UserService -> deleteUser(userId) method is called. Finding the user");

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			
			logger.error("Can't find the user id from the database");
			
			throw new Exception(userId);
		}

		logger.info("User details found. Deleting the user from the database -> users");
		
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers() {

		logger.info("UserService -> getUsers() method is called. Collection all users details");
		
		List<UserDto> returnValue = new ArrayList<>();

		Iterable<UserEntity> iterableObjects = userRepository.findAll();

		for (UserEntity userEntity : iterableObjects) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}

		logger.info("Users' details found. Returning users to the UserController via List<UserDto>");
		
		return returnValue;
	}

}
