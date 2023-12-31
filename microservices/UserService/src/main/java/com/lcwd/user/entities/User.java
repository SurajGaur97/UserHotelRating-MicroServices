package com.lcwd.user.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MICRO-USERS")
public class User {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "name", length = 20)
	private String name;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "about")
	private String about;

}
