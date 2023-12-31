package com.lcwd.user.services;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwd.user.dto.UserResponceDto;
import com.lcwd.user.entities.Hotel;
import com.lcwd.user.entities.Rating;
import com.lcwd.user.entities.User;
import com.lcwd.user.exceptions.ResourceNotFoundException;
import com.lcwd.user.external.feignServices.HotelService;
import com.lcwd.user.external.feignServices.RatingService;
import com.lcwd.user.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RatingService ratingService;
	
	@Override
	public User saveUser(User user) {
		String userId = UUID.randomUUID().toString();
		user.setId(userId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public User getUser(String userId) {
		return userRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User with given id does not exist !! :" + userId));
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User updateUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @implNote: It is an example of FienClient and RestTempate. For implementing client side communication 
	 * we can use 'RestTemplate' or 'FienClient'. 
	 * 1.For implementing RestTemplate: we have to create a bean inside a configuration class and then
	 * use it by auto-wiring its object in our classes. 
	 * 2.For implementing FienCleint: we need to add dependency and then we need to create a FienClient Interface 
	 * with annotation @FientClient. Inside the interface we need to write method declaration not exactly like the 
	 * service we wants call. Then after we need to auto-wire the interface object and then can be used further. 
	 */
	@Override
	public UserResponceDto getAllUserDetails(String userId) {
		//Getting users from database		
		User user = userRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User with given id does not exist !! :" + userId));
		
		//fetching rating of above user from RATING-SERVICE
		//http://RATING-SERVICE/ratings/getRatingsByUserId?userId=userId
		Rating[] ratingsArr = restTemplate.getForObject("http://RATING-SERVICE/ratings/getRatingsByUserId?userId=" + userId, Rating[].class);
		
		//List<Rating> ratingList = Arrays.stream(ratingsArr).toList();		//type-1
		List<Rating> ratingList = Arrays.asList(ratingsArr);				//type-2
		logger.info("{}", ratingList);
		
//		List<Rating> ratings = ratingList.stream().map(rating -> {			//type-1
//			Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/getHotel?hotelId=" + rating.getHotelId(), Hotel.class);
//			rating.setHotel(hotel);
//			
//			return rating;
//		}).collect(Collectors.toList());
		
//		ratingList.forEach(rating -> {										//type-2
//			Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/getHotel?hotelId=" + rating.getHotelId(), Hotel.class);
//			rating.setHotel(hotel);
//		});
		
		ratingList.stream().forEach(rating -> {	
			//fetching rating of above user from RATING-SERVICE
			//http://HOTEL-SERVICE/hotels/getHotel?hotelId=hotelId//type-3
			
			//Old RestTemplate Client Approach
			//Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/getHotel?hotelId=" + rating.getHotelId(), Hotel.class);
			
			//New 'OpenFeinClient' Approach
			Hotel hotel = hotelService.getHotelById(rating.getHotelId());
			
			rating.setHotel(hotel);
		});
		
		UserResponceDto userRes = UserResponceDto.builder()
				.user(user)
				.ratings(ratingList)
				.build();
		
		return userRes;
	}

	@Override
	public Rating saveRating(Rating rating) {
		ResponseEntity<Rating> res = ratingService.createRating(rating);
		Rating ratng;
		if(res.getStatusCode().is2xxSuccessful() && res.hasBody()) {
			ratng = res.getBody();
			return ratng;
		} else {
			return null;
		}
	}
	
}
