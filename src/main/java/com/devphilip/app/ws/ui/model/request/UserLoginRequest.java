package com.devphilip.app.ws.ui.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {
	private String email;
	private String password;
}
