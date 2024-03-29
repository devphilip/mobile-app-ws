package com.devphilip.app.ws.service;

import java.util.List;

import com.devphilip.app.ws.shared.dto.AddressDto;

public interface AddressService {
	List<AddressDto> getAddresses(String userId);
	AddressDto getAddress(String addressId);
}
