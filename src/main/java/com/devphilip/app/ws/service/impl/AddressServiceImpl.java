package com.devphilip.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devphilip.app.ws.io.entity.AddressEntity;
import com.devphilip.app.ws.io.entity.UserEntity;
import com.devphilip.app.ws.io.repository.AddressRepository;
import com.devphilip.app.ws.io.repository.UserRepository;
import com.devphilip.app.ws.service.AddressService;
import com.devphilip.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) return returnValue;
 
        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for(AddressEntity addressEntity: addresses) {
            returnValue.add( modelMapper.map(addressEntity, AddressDto.class) );
        }
        
        return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto returnValue = null;

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        
        if(addressEntity != null) {
            returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
        }
 
        return returnValue;
	}

}
