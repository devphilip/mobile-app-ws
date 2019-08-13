package com.devphilip.app.ws.shared.dto;

import lombok.Data;

@Data
public class AddressDto {
	private long id;
	private String addressId;
	private String street;
	private String city;
	private String state;
	private String country;
	private String postalCode;
	private String type;
	private UserDto userDetails;
}
