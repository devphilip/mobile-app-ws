package com.devphilip.app.ws.ui.model.request;

import lombok.Data;

@Data
public class AddressRequest {
	private String street;
	private String city;
	private String state;
	private String country;
	private String postalCode;
	private String type;
}
