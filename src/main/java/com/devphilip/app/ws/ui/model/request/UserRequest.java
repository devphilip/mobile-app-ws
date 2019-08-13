package com.devphilip.app.ws.ui.model.request;

import java.util.List;

import lombok.Data;

@Data
public class UserRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<AddressRequest> addresses;
}
