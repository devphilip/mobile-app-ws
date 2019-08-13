package com.devphilip.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.devphilip.app.ws.exception.UserServiceException;
import com.devphilip.app.ws.io.entity.UserEntity;
import com.devphilip.app.ws.io.repository.UserRepository;
import com.devphilip.app.ws.service.UserService;
import com.devphilip.app.ws.shared.Utils;
import com.devphilip.app.ws.shared.dto.AddressDto;
import com.devphilip.app.ws.shared.dto.UserDto;
import com.devphilip.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		List<UserDto> returnedValue = new ArrayList<>();
		if(page >0) page = page-1;
		Pageable pageableRequest = PageRequest.of(page, limit);
		List<UserEntity> users = userRepository.findAll(pageableRequest).getContent();
		
		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnedValue.add(userDto);
		}
		
		return returnedValue;
	}

	@Override
	public UserDto createUser(UserDto user) {
				
		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Record already exists");		
		
		for (int i = 0; i < user.getAddresses().size(); i++) {
			AddressDto addressDto = user.getAddresses().get(i);;
			addressDto.setUserDetails(user);
			addressDto.setAddressId(utils.generateAdressId(30));
			user.getAddresses().set(i, addressDto);
		}

		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword())); 

		UserEntity storedUser = userRepository.save(userEntity);

		UserDto userDto = modelMapper.map(storedUser, UserDto.class);
		
		return userDto;
	}
	
	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		UserDto returnedUser = new UserDto();
		BeanUtils.copyProperties(userEntity, returnedUser);
		return returnedUser;
	}


	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnedUser = new UserDto(); 
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) throw new UsernameNotFoundException(userId);
		BeanUtils.copyProperties(userEntity, returnedUser);
		return returnedUser;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserDto returnedUser = new UserDto(); 
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) 
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUser, returnedUser);
		
		return returnedUser;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userRepository.delete(userEntity);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

}
