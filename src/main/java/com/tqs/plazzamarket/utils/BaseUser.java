package com.tqs.plazzamarket.utils;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@MappedSuperclass
public abstract class BaseUser implements Serializable {
	private static final long serialVersionUID = -8426863896784752433L;

	@Id
	@NotNull(message = "Username is mandatory!")
	@NotBlank(message = "Username is mandatory!")
	private String username;

	@NotNull(message = "Name is mandatory!")
	@NotBlank(message = "Name is mandatory!")
	private String name;

	@NotNull(message = "Email is mandatory!")
	@NotBlank(message = "Email is mandatory!")
	@Email(message = "Email format incorrect!")
	private String email;

	@NotNull(message = "Password is mandatory!")
	@NotBlank(message = "Password is mandatory!")
	@Length(min = 8, message = "Password must have at least 8 characters!")
	private String password;

	@NotNull(message = "Address is mandatory!")
	@NotBlank(message = "Address is mandatory!")
	private String address;

	@NotNull(message = "Zip code is mandatory!")
	@NotBlank(message = "Zip code is mandatory!")
	private String zipCode;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;;
		BaseUser baseUser = (BaseUser) obj;
		return Objects.equals(username, baseUser.username) && Objects.equals(name, baseUser.name)
				&& Objects.equals(email, baseUser.email) && Objects.equals(password, baseUser.password)
				&& Objects.equals(address, baseUser.address) && Objects.equals(zipCode, baseUser.zipCode);
	}
}