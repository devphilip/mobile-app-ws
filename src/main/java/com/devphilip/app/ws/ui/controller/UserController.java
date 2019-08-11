package com.devphilip.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devphilip.app.ws.exception.UserServiceException;
import com.devphilip.app.ws.service.UserService;
import com.devphilip.app.ws.shared.dto.UserDto;
import com.devphilip.app.ws.ui.model.request.UserRequest;
import com.devphilip.app.ws.ui.model.response.ErrorMessages;
import com.devphilip.app.ws.ui.model.response.OperationStatusModel;
import com.devphilip.app.ws.ui.model.response.RequestOperationStatus;
import com.devphilip.app.ws.ui.model.response.UserRestResponse;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRestResponse> getUsers(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRestResponse> returnedUsers = new ArrayList<UserRestResponse>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for (UserDto userDto : users) {
			UserRestResponse userRestResponse = new UserRestResponse();
			BeanUtils.copyProperties(userDto, userRestResponse);
			returnedUsers.add(userRestResponse);
		}

		return returnedUsers; 		
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRestResponse createUser(@RequestBody UserRequest userRequest) throws Exception {
		
		UserRestResponse userResponse = new UserRestResponse();
		
		if (userRequest.getFirstName().isEmpty()) 
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userRequest, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, userResponse);
		
		return userResponse;
	}
	
	@GetMapping(path = "/{userId}", 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRestResponse getUser(@PathVariable String userId) {
		UserRestResponse userResponse = new UserRestResponse();
		UserDto userDto = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDto, userResponse);
		return userResponse; 
	}
	
	@PutMapping(path = "/{userId}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRestResponse updateUser(@PathVariable String userId, @RequestBody UserRequest userRequest) {
		
UserRestResponse userResponse = new UserRestResponse();
		
		if (userRequest.getFirstName().isEmpty()) 
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userRequest, userDto);
		
		UserDto updatedUser = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(updatedUser, userResponse);
		
		return userResponse;
	}
	
	@DeleteMapping(path = "/{userId}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String userId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(userId);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
}


