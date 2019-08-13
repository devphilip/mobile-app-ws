package com.devphilip.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import com.devphilip.app.ws.service.AddressService;
import com.devphilip.app.ws.service.UserService;
import com.devphilip.app.ws.shared.dto.AddressDto;
import com.devphilip.app.ws.shared.dto.UserDto;
import com.devphilip.app.ws.ui.model.request.UserRequest;
import com.devphilip.app.ws.ui.model.response.AddressRestResponse;
import com.devphilip.app.ws.ui.model.response.ErrorMessages;
import com.devphilip.app.ws.ui.model.response.OperationStatusModel;
import com.devphilip.app.ws.ui.model.response.RequestOperationStatus;
import com.devphilip.app.ws.ui.model.response.UserRestResponse;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;

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
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRequest, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
		userResponse = modelMapper.map(createdUser, UserRestResponse.class);
		
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

	@GetMapping(path = "/{userId}/addresses", 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<AddressRestResponse> getUserAddresses(@PathVariable String userId) {
		List<AddressRestResponse> returnedValue = new ArrayList<>();
		List<AddressDto> addressesDto = addressService.getAddresses(userId);
		
		if (addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressRestResponse>>() {}.getType();
			returnedValue = new ModelMapper().map(addressesDto, listType);
		}
		
		return returnedValue; 
	}
	
	@GetMapping(path = "/{userId}/addresses/{addressId}", 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public AddressRestResponse getUserAddress(@PathVariable String addressId) {
		AddressDto addressDto = addressService.getAddress(addressId);
		
		ModelMapper modelMapper = new ModelMapper();
		
		return modelMapper.map(addressDto, AddressRestResponse.class); 
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


