package com.devphilip.app.ws.shared.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class UserDto implements Serializable {
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 7069181197423073640L;
	
	private Long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String encryptedPassword;
	private String emailVerificationToken;
	private boolean emailVerificationStatus = false;
	List<addressDto> addresses;

}
