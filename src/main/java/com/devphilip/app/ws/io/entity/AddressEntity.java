package com.devphilip.app.ws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "addresses")
public class AddressEntity implements Serializable {

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(length=30, nullable=false)
	private String addressId;
	
	@Column(length=100, nullable=false)
	private String street;
	
	@Column(length=15, nullable=false)
	private String city;
	
	@Column(length=30, nullable=false)
	private String state;
	
	@Column(length=15, nullable=false)
	private String country;
	
	@Column(length=7, nullable=false)
	private String postalCode;
	
	@Column(length=10, nullable=false)
	private String type;
	
	@ManyToOne
	@JoinColumn(name="users_id")
	private UserEntity userDetails;
}
 