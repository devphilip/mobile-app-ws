package com.devphilip.app.ws.ui.model.response;

import lombok.Data;

@Data
public class AddressRestResponse {
	private String addressId;
	private String street;
	private String city;
	private String state;
	private String country;
	private String postalCode;
	private String type;
}
