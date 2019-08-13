package com.devphilip.app.ws.shared.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
		
	private Long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String encryptedPassword;
	private String emailVerificationToken;
	private boolean emailVerificationStatus = false;
	private List<AddressDto> addresses;
}
