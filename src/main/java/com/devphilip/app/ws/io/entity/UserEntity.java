package com.devphilip.app.ws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity(name = "users")
public class UserEntity implements Serializable {

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 360950226426587082L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false, length = 50)
	private String firstName;
	
	@Column(nullable = false, length = 50)
	private String lastName;
	
	@Column(nullable = false, length = 120, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
//	@Column(nullable = false, columnDefinition = "boolean default false")
//	private boolean emailVerificationStatus;
	
	@Column(nullable = false)
	private boolean emailVerificationStatus = false;
	
//	@OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
//	List<addressEntity> addresses;

}
