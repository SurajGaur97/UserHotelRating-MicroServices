package com.lcwd.user.dto;

import java.util.List;

import com.lcwd.user.entities.Rating;
import com.lcwd.user.entities.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponceDto {
	
	private User user;

	private List<Rating> ratings;

}
