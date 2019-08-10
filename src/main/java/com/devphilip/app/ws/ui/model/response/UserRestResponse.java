package com.devphilip.app.ws.ui.model.response;

import lombok.Data;

@Data
public class UserRestResponse {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
}
